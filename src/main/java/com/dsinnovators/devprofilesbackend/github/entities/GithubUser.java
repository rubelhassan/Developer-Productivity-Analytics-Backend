package com.dsinnovators.devprofilesbackend.github.entities;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class GithubUser {
    private String name;
    private String login;
    private String email;
    private String bio;
    private String avatarUrl;
    private String websiteUrl;
    private String company;
    private String location;
    private Date createdAt;
    private OrganizationConnection organizationConnections = new OrganizationConnection();
    private GenericConnection followers;
    private GenericConnection following;
    private GenericConnection starredRepositories;
    private GenericConnection repositoriesContributedTo;
    private GenericConnection gists;
    private GenericConnection issues;
    private RepositoriesWithCount pinnedItems = new RepositoriesWithCount();
    private RepositoriesWithCount topRepositories = new RepositoriesWithCount();
    private RepositoriesWithCount repositories = new RepositoriesWithCount();
    private GithubContributionSummary contributionsSummary;
    private GithubContributionSummary contributionsWeekly;
}
