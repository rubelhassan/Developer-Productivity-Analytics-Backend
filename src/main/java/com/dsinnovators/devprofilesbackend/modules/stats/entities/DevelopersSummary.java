package com.dsinnovators.devprofilesbackend.modules.stats.entities;

import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DevelopersSummary {
    private long totalDevelopers;
    private long totalCommitContributions;
    private long totalPullRequestContributions;
    private long totalPullRequestReviewContributions;
    private long totalIssueContributions;
    private long totalRepositoryContributions;
    private long totalRepositoriesWithContributedIssues;
    private long totalRepositoriesWithContributedCommits;
    private long totalRepositoriesWithContributedPullRequests;
    private long totalRepositoriesWithContributedPullRequestReviews;

    public DevelopersSummary(long totalDevelopers,
                             long totalCommitContributions, long totalPullRequestContributions,
                             long totalPullRequestReviewContributions, long totalIssueContributions,
                             long totalRepositoryContributions, long totalRepositoriesWithContributedIssues,
                             long totalRepositoriesWithContributedCommits,
                             long totalRepositoriesWithContributedPullRequests,
                             long totalRepositoriesWithContributedPullRequestReviews) {
        this.totalDevelopers = totalDevelopers;
        this.totalCommitContributions = totalCommitContributions;
        this.totalPullRequestContributions = totalPullRequestContributions;
        this.totalPullRequestReviewContributions = totalPullRequestReviewContributions;
        this.totalIssueContributions = totalIssueContributions;
        this.totalRepositoryContributions = totalRepositoryContributions;
        this.totalRepositoriesWithContributedIssues = totalRepositoriesWithContributedIssues;
        this.totalRepositoriesWithContributedCommits = totalRepositoriesWithContributedCommits;
        this.totalRepositoriesWithContributedPullRequests = totalRepositoriesWithContributedPullRequests;
        this.totalRepositoriesWithContributedPullRequestReviews = totalRepositoriesWithContributedPullRequestReviews;
    }

    private int totalOrganizationsContributedTo;
    private int totalRepositories;
    private int totalStarsOfRepositories;

    // all in last 7 days
    @Builder.Default
    List<CountOfEntity> topOrganizationsByRepositoriesCount = new ArrayList<>();

    @Builder.Default
    List<CountOfEntity> topLanguagesByRepositoriesCount = new ArrayList<>();

    @Builder.Default
    List<CountOfEntity> topTopicsByRepositoryCount = new ArrayList<>();

    @Builder.Default
    List<ProfileRank> topProfilesByRepositories = new ArrayList<>();

    @Builder.Default
    List<ProfileRank> topProfilesByStars = new ArrayList<>();

    @Builder.Default
    List<ProfileRank> topProfilesByCommits = new ArrayList<>();

    @Builder.Default
    List<ProfileRank> topProfilesByIssues = new ArrayList<>();

    @Builder.Default
    List<ProfileRank> topProfilesByPullRequests = new ArrayList<>();

    @Builder.Default
    List<ProfileRank> topProfilesByRepositoryCreation = new ArrayList<>();

    @Builder.Default
    List<RepositoryLite> topRepositoriesByStars = new ArrayList<>();
}
