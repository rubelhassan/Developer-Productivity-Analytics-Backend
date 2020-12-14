package com.dsinnovators.devprofilesbackend.modules.developers;

import com.dsinnovators.devprofilesbackend.github.GithubGraphqlClient;
import com.dsinnovators.devprofilesbackend.github.entities.GithubUser;
import com.dsinnovators.devprofilesbackend.modules.developers.entities.*;
import com.dsinnovators.devprofilesbackend.utils.DeveloperNotFoundException;
import com.dsinnovators.devprofilesbackend.utils.GithubUserNotFound;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Slf4j
@Service
@AllArgsConstructor
public class DeveloperService {
    private GithubGraphqlClient githubGraphqlClient;
    private DeveloperRepository developerRepository;
    private ProfileRepository profileRepository;

    public Developer getProfile(Long developerId) throws DeveloperNotFoundException, GithubUserNotFound {
        Developer developer = developerRepository.findById(developerId).orElseThrow(
                () -> new DeveloperNotFoundException(developerId));
        Profile existingProfile = developer.githubProfile();
        // TODO: fetch from github server should be moved to a scheduled job
        developer.addProfile(fetchGithubUserProfile(existingProfile, developer.getGithubAccessToken()));
        developer.getProfiles().remove(existingProfile);
        developerRepository.save(developer);
        return developer;
    }

    private Profile fetchGithubUserProfile(Profile existingProfile, String accessToken) throws GithubUserNotFound {
        GithubUser githubData = githubGraphqlClient.fetchUserProfileWithContributions(accessToken);
        Profile githubProfile = Profile.from(githubData);
        if (existingProfile != null) {
            githubProfile.setId(existingProfile.getId());
            githubProfile.setOrganizationMap(existingProfile.getOrganizationMap());
        }
        populateProfileWithOrganizations(githubProfile, githubData);
        populateProfileWithRepositories(githubProfile, githubData);
        githubProfile.setWeeklyContributions(WeeklyContribution.from(githubData.getContributionsWeekly(),
                               ofNullable(existingProfile).map(Profile::getWeeklyContributions).orElse(null)));
        return githubProfile;
    }

    private void populateProfileWithRepositories(Profile profile, GithubUser user) {
        Map<String, Repository> repositoryMap =
                user.getRepositories().getRepositories()
                    .stream()
                    .map(Repository::from)
                    .peek(repo -> repo.setProfile(profile))
                    .collect(toMap(Repository::getRepositoryId, identity()));

        user.getPinnedItems().getRepositories().forEach(repo -> {
            if (repositoryMap.containsKey(repo)) {
                repositoryMap.get(repo).setPinned(true);
            } else {
                Repository repository = Repository.from(repo);
                repository.setPinned(true);
                repository.setProfile(profile);
                repositoryMap.put(repository.getRepositoryId(), repository);
            }
        });

        user.getTopRepositories().getRepositories().forEach(repo -> {
            if (repositoryMap.containsKey(repo)) {
                repositoryMap.get(repo).setPinned(true);
            } else {
                Repository repository = Repository.from(repo);
                repository.setTop(true);
                repository.setProfile(profile);
                repositoryMap.put(repository.getRepositoryId(), repository);
            }
        });

        profile.setRepositories(new ArrayList<>(repositoryMap.values()));
    }

    private void populateProfileWithOrganizations(Profile profile, GithubUser user) {
        Map<String, Organization> organizationMap = profile.getOrganizationMap();
        List<Organization> organizations =
                user.getOrganizationConnections()
                    .getOrganizations().stream()
                    .map(Organization::from)
                    .peek(org -> {
                        if (organizationMap.containsKey(org.getGithubId())) {
                            org.setId(organizationMap.get(org.getGithubId()).getId());
                        }
                        org.getProfiles().add(profile);
                    })
                    .collect(Collectors.toList());
        profile.setOrganizations(organizations);
    }
}
