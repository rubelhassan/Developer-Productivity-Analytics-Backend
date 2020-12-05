package com.dsinnovators.devprofilesbackend.controllers;

import com.dsinnovators.devprofilesbackend.github.GithubGraphqlClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value="profiles", produces = "application/json")
@AllArgsConstructor
public class ProfileController {
    private GithubGraphqlClient githubGraphqlClient;

    @GetMapping
    ResponseEntity<String> getUserProfile() throws JsonProcessingException {
        // TODO: Need to refactor with stored token and parameterized profile id
        String token = "place test token here";
        return ok(githubGraphqlClient.fetchUserProfileWithContributions(token));
    }
}
