package com.dsinnovators.devprofilesbackend.modules.stats.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProfileRank {
    private String githubLogin;
    private String name;
    private int totalStars;
    private int totalRepositories;
    private int totalCommitContributionsWeekly;
    private int totalPullRequestContributionsWeekly;
    private int totalIssueContributionsWeekly;
    private int totalRepositoryContributionsWeekly;
    private int totalPullRequestReviewContributionsWeekly;
}
