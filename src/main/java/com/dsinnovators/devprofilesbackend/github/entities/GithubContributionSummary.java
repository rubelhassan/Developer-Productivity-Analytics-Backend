package com.dsinnovators.devprofilesbackend.github.entities;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class GithubContributionSummary {
    @JsonAlias("hasAnyContributions")
    private boolean hasAnyContributions;

    @JsonAlias("hasAnyRestrictedContributions")
    private boolean hasAnyRestrictedContributions;

    private int totalCommitContributions;
    private int totalPullRequestContributions;
    private int totalPullRequestReviewContributions;
    private int totalIssueContributions;
    private int totalRepositoryContributions;
    private int totalRepositoriesWithContributedIssues;
    private int totalRepositoriesWithContributedCommits;
    private int totalRepositoriesWithContributedPullRequests;
    private int totalRepositoriesWithContributedPullRequestReviews;
    private int restrictedContributionsCount;
    private Date startedAt;
    private Date endedAt;
    private JsonNode contributionCalendar;

    public String getContributionCalenderString() {
        if (contributionCalendar != null) {
            return contributionCalendar.toString();
        }
        return null;
    }
}
