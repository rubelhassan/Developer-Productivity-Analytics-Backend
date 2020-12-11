package com.dsinnovators.devprofilesbackend.github.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Repository {
    private String name;
    private String description;
    private Integer stargazerCount;
    private String nameWithOwner;
    private boolean isPrivate;
    private boolean isFork;
    private int forkCount;
    private RepositoryOwner owner;
    private Language primaryLanguage;
    private RepositoryTopicConnection repositoryTopics;
    private GenericConnection pullRequests;
    private GenericConnection issues;
    private GenericConnection watchers;
}
