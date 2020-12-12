package com.dsinnovators.devprofilesbackend.modules.profiles;

import com.dsinnovators.devprofilesbackend.github.GithubGraphqlClient;
import com.dsinnovators.devprofilesbackend.github.entities.GithubResponseData;
import com.dsinnovators.devprofilesbackend.github.entities.User;
import com.dsinnovators.devprofilesbackend.modules.profiles.entities.Organization;
import com.dsinnovators.devprofilesbackend.modules.profiles.entities.Profile;
import com.dsinnovators.devprofilesbackend.modules.profiles.entities.Repository;
import com.dsinnovators.devprofilesbackend.modules.profiles.entities.WeeklyContribution;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Service
@AllArgsConstructor
public class ProfileService {
    private GithubGraphqlClient githubGraphqlClient;
    private ProfileRepository profileRepository;

    public Profile getProfile(Long id) throws JsonProcessingException {
        String token = "place you token here";
        // TODO: should load from database as we aren't going to request github everytime
        GithubResponseData githubData = githubGraphqlClient.fetchUserProfileWithContributions(token);
        Profile profile = Profile.from(githubData);
        if (profile.getId() == null) {
            profile = profileRepository.save(profile);
        }
        profile = addOtherPropertiesAndPersist(profile, githubData);
        return profile;
    }

    // TODO: need refactor badly
    private Profile addOtherPropertiesAndPersist(Profile profile, GithubResponseData githubData) throws JsonProcessingException {
        User user = githubData.getData().getUser();
        profile.setWeeklyContributions(WeeklyContribution.from(user.getContributionsWeekly(), profile));
        JsonNode contributionsCalendar = profile.getWeeklyContributions().getContributionsCalendar();
        profile.setOrganizations(user.getOrganizationConnections().getOrganizations().stream()
                .map(Organization::from)
                .peek(org -> org.setProfile(profile))
                .collect(Collectors.toList()));
        profile.setRepositoryMap(user.getRepositories().getRepositories().stream()
                .map(Repository::from)
                .peek(repo -> repo.setProfile(profile))
                .collect(toMap(Repository::getRepositoryId, identity())));
        user.getPinnedItems().getRepositories().forEach(repo -> {
            if (profile.getRepositoryMap().containsKey(repo)) {
                profile.getRepositoryMap().get(repo).setPinned(true);
            } else {
                Repository repository = Repository.from(repo);
                repository.setPinned(true);
                repository.setProfile(profile);
                profile.getRepositoryMap().put(repository.getRepositoryId(), repository);
            }
        });
        user.getTopRepositories().getRepositories().forEach(repo -> {
            if (profile.getRepositoryMap().containsKey(repo)) {
                profile.getRepositoryMap().get(repo).setPinned(true);
            } else {
                Repository repository = Repository.from(repo);
                repository.setTop(true);
                repository.setProfile(profile);
                profile.getRepositoryMap().put(repository.getRepositoryId(), repository);
            }
        });
        profile.setRepositories(new ArrayList<>(profile.getRepositoryMap().values()));
        profileRepository.save(profile);
        profile.getWeeklyContributions().setContributionsCalendar(contributionsCalendar);
        return profile;
    }
}
