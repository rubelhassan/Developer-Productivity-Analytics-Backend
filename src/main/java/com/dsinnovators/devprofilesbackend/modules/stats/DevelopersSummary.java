package com.dsinnovators.devprofilesbackend.modules.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DevelopersSummary {
    private long totalCommitContributions;
    private long totalPullRequestContributions;
    private long totalPullRequestReviewContributions;
    private long totalIssueContributions;
    private long totalRepositoryContributions;
    private long totalRepositoriesWithContributedIssues;
    private long totalRepositoriesWithContributedCommits;
    private long totalRepositoriesWithContributedPullRequests;
    private long totalRepositoriesWithContributedPullRequestReviews;

    // TODO: what about keep another for 7 days only
    // TODO: total organizations
}
