package com.dsinnovators.devprofilesbackend.github.entities;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class User {
    private String name;
    private String login;
    private String email;
    private String bio;
    private String avatarUrl;
    private String websiteUrl;
    private String company;
    private String location;
    private Date createdAt;
    private OrganizationConnection organizationConnections;
    private GenericConnection followers;
    private GenericConnection following;
    private GenericConnection starredRepositories;
    private GenericConnection repositoriesContributedTo;
    private GenericConnection gists;
    private GenericConnection issues;
    private RepositoriesWithCount pinnedItems;
    private RepositoriesWithCount topRepositories;
    private RepositoriesWithCount repositories;
    private JsonNode allContributions;
    private JsonNode weeklyContributions;
}
