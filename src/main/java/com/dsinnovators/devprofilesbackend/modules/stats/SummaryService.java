package com.dsinnovators.devprofilesbackend.modules.stats;

import com.dsinnovators.devprofilesbackend.modules.developers.OrganizationRepository;
import com.dsinnovators.devprofilesbackend.modules.developers.ProfileRepository;
import com.dsinnovators.devprofilesbackend.modules.developers.RepositoryEntityRepository;
import com.dsinnovators.devprofilesbackend.modules.developers.entities.Organization;
import com.dsinnovators.devprofilesbackend.modules.developers.entities.Repository;
import com.dsinnovators.devprofilesbackend.modules.stats.entities.CountOfEntity;
import com.dsinnovators.devprofilesbackend.modules.stats.entities.DevelopersSummary;
import com.dsinnovators.devprofilesbackend.modules.stats.entities.ProfileRank;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@Service
@AllArgsConstructor
public class SummaryService {
    private RepositoryEntityRepository repositoryEntityRepository;
    private ProfileRepository profileRepository;
    private OrganizationRepository organizationRepository;

    public DevelopersSummary generateStats() {
        DevelopersSummary developersSummary = profileRepository.accumulatedValuesQuery();
        summarizeTopProfilesByDifferentCriteria(developersSummary);
        summarizeTopEntitiesByRepositories(developersSummary);
        return developersSummary;
    }

    private void summarizeTopEntitiesByRepositories(DevelopersSummary developersSummary) {
        List<Repository> repositories = repositoryEntityRepository.findAll();
        if (isEmpty(repositories)) {
            return;
        }
        Map<String, Organization> organizationMap = getOrganizationsByLogin();
        Map<Organization, Set<String>> organizationRepositoryMap = new HashMap<>();
        Map<String, Set<String>> languageRepositoryMap = new HashMap<>();
        repositories.forEach((repository)-> {
            if (organizationMap.containsKey(repository.getOwner())) {
                organizationRepositoryMap
                        .computeIfAbsent(organizationMap.get(repository.getOwner()), k -> new HashSet<>())
                        .add(repository.getName());
            }
            if (repository.getPrimaryLanguage() != null) {
                languageRepositoryMap
                        .computeIfAbsent(repository.getPrimaryLanguage(), k -> new HashSet<>())
                        .add(repository.getName());
            }
        });
        developersSummary.setTotalOrganizationsContributedTo(organizationMap.size());
        developersSummary.setTopOrganizationsByRepositoriesCount(findTopNEntity(
                organizationRepositoryMap.entrySet()
                                         .stream()
                                         .map(entry -> new CountOfEntity(
                                                 entry.getKey().getName(), entry.getValue().size())
                                         ), 5, comparingInt(CountOfEntity::getCount).reversed()));
        developersSummary.setTopLanguagesByRepositoriesCount(findTopNEntity(
                languageRepositoryMap.entrySet().stream()
                                     .map(entry -> new CountOfEntity(
                                             entry.getKey(), entry.getValue().size())
                                     ), 20, comparingInt(CountOfEntity::getCount).reversed()));
    }

    private Map<String, Organization> getOrganizationsByLogin() {
        Map<String, Organization> organizationMap = new HashMap<>();
        List<Organization> organizations = organizationRepository.findAll();
        if (!isEmpty(organizations)) {
            organizations.forEach(organization -> organizationMap.put(organization.getLogin(), organization));
        }
        return organizationMap;
    }

    private void summarizeTopProfilesByDifferentCriteria(DevelopersSummary developersSummary) {
        List<ProfileRank> profileRankList = profileRepository.githubProfileRanksQuery();
        developersSummary.setTopProfilesByCommits(findTopNEntity(
                profileRankList.stream(), 5, comparingInt(ProfileRank::getTotalCommitContributionsWeekly).reversed()));
        developersSummary.setTopProfilesByIssues(findTopNEntity(
                profileRankList.stream(), 5, comparingInt(ProfileRank::getTotalIssueContributionsWeekly).reversed()));
        developersSummary.setTopProfilesByPullRequests(findTopNEntity(
                profileRankList.stream(), 5, comparingInt(ProfileRank::getTotalPullRequestContributionsWeekly).reversed()));
        developersSummary.setTopProfilesByRepositoryCreation(findTopNEntity(
                profileRankList.stream(), 5, comparingInt(ProfileRank::getTotalRepositoryContributionsWeekly).reversed()));
        developersSummary.setTopProfilesByStars(findTopNEntity(
                profileRankList.stream(), 5, comparingInt(ProfileRank::getTotalStars).reversed()));
        developersSummary.setTopProfilesByRepositories(findTopNEntity(
                profileRankList.stream(), 5, comparingInt(ProfileRank::getTotalRepositories).reversed()));
    }

    private <T> List<T> findTopNEntity(Stream<T> entities, int n, Comparator<? super T> comparator) {
        return entities.sorted(comparator)
                       .limit(n)
                       .collect(toList());
    }
}
