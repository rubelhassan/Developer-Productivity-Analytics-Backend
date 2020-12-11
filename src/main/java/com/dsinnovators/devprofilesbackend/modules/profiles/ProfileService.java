package com.dsinnovators.devprofilesbackend.modules.profiles;

import com.dsinnovators.devprofilesbackend.github.GithubGraphqlClient;
import com.dsinnovators.devprofilesbackend.modules.profiles.entities.Profile;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProfileService {
    private GithubGraphqlClient githubGraphqlClient;
    private ProfileRepository profileRepository;

    public Profile getProfile(Long id) throws JsonProcessingException {
        String token = "place you token here";
        Profile profile = Profile.from(githubGraphqlClient.fetchUserProfileWithContributions(token));
        profile = profileRepository.save(profile);
        return profile;
    }
}
