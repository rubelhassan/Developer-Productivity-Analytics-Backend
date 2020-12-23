package com.dsinnovators.devprofilesbackend.github.entities;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class GithubRepository {
    private String repositoryId;
    private String name;
    private String description;
    private String url;
    private Integer stargazerCount;
    private String nameWithOwner;

    @JsonAlias("isPrivate")
    private boolean isPrivate;

    @JsonAlias("isFork")
    private boolean isFork;

    private int forkCount;
    private RepositoryOwner owner;
    private Language primaryLanguage;
    private RepositoryTopicConnection repositoryTopics;
    private GenericConnection pullRequests;
    private GenericConnection issues;
    private GenericConnection watchers;
}
