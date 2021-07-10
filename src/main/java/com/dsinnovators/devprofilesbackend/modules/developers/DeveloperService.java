package com.dsinnovators.devprofilesbackend.modules.developers;

import com.dsinnovators.devprofilesbackend.github.GithubGraphqlClient;
import com.dsinnovators.devprofilesbackend.github.entities.GithubRepository;
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
import java.util.function.Consumer;
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

    public Developer getProfile(Long developerId) throws DeveloperNotFoundException, GithubUserNotFound {
        Developer developer = developerRepository.findById(developerId).orElseThrow(
                () -> new DeveloperNotFoundException(developerId));
        Profile existingProfile = developer.githubProfile();
        // TODO: fetch from github server should be moved to a scheduled job
        developer.getProfiles().remove(existingProfile);
        developer.addProfile(fetchGithubUserProfile(existingProfile, developer.getGithubAccessToken()));
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
        populateProfileWithRepositories(githubProfile, githubData, ofNullable(existingProfile)
                .map(Profile::getRepositories).orElse(new ArrayList<>()));
        githubProfile.setWeeklyContributions(WeeklyContribution.from(githubData.getContributionsWeekly(),
                               ofNullable(existingProfile).map(Profile::getWeeklyContributions).orElse(null)));
        return githubProfile;
    }

    private void populateProfileWithRepositories(Profile profile, GithubUser user, List<Repository> repositories) {
        Map<String, Repository> repositoryMap =
                user.getRepositories().getRepositories()
                    .stream()
                    .map(Repository::from)
                    .peek(repo -> repo.setProfile(profile))
                    .collect(toMap(Repository::getRepositoryId, identity()));
        repositories.forEach(repo -> {
            if (repositoryMap.containsKey(repo.getRepositoryId())) {
                repositoryMap.get(repo.getRepositoryId()).setId(repo.getId());
            }
        });
        addPropertyToRepositories(profile, repositoryMap, user.getTopRepositories().getRepositories(),
                                  (repository -> repository.setTop(true)));
        addPropertyToRepositories(profile, repositoryMap, user.getPinnedItems().getRepositories(),
                                  (repository -> repository.setPinned(true)));
        ArrayList<Repository> repos = new ArrayList<>(repositoryMap.values());
        repos.forEach(repository -> {
            if (profile.getLogin().equals(repository.getOwner())) {
                profile.setTotalStars(profile.getTotalStars() + repository.getStargazerCount());
                profile.setTotalRepositories(profile.getTotalRepositories() + 1);
            } else {
                profile.setTotalRepositoriesContributedTo(profile.getRepositoriesContributedTo() + 1);
            }
        });
        profile.setRepositories(repos);
    }

    private void addPropertyToRepositories(Profile profile, Map<String, Repository> repositoryMap,
                                           List<GithubRepository> repositories, Consumer<Repository> consumer) {
        repositories.forEach(repo -> {
            if (repositoryMap.containsKey(repo.getRepositoryId())) {
                consumer.accept(repositoryMap.get(repo.getRepositoryId()));
            } else {
                Repository repository = Repository.from(repo);
                consumer.accept(repository);
                repository.setProfile(profile);
                repositoryMap.put(repository.getRepositoryId(), repository);
            }
        });
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

    public Developer createProfile(Developer developer) {
        return developerRepository.save(developer);
    }

    public Developer updateToken(Long id, String code) throws DeveloperNotFoundException, GithubUserNotFound {
        Developer developer = developerRepository.findById(id)
                                                 .orElseThrow(() -> new DeveloperNotFoundException(id));
        developer.setGithubAccessToken(githubGraphqlClient.getAccessTokenFromGithub(code));
        Profile existingProfile = developer.githubProfile();
        developer.getProfiles().remove(existingProfile);
        developer.addProfile(fetchGithubUserProfile(existingProfile, developer.getGithubAccessToken()));
        return developerRepository.save(developer);
    }
}
