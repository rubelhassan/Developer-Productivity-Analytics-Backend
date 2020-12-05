package com.dsinnovators.devprofilesbackend.controllers;

import com.dsinnovators.devprofilesbackend.github.GithubGraphqlClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {
    @Autowired
    private GithubGraphqlClient githubGraphqlClient;

    @GetMapping("/profiles")
    String getUserProfile() throws JsonProcessingException {
        return githubGraphqlClient.fetchUserProfileWithContributions("97dcb67c1aef75929b6e8ffb9c7de47f627f7962");
    }
}
