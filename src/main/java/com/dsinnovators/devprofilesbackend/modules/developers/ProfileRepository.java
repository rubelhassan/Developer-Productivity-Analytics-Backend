package com.dsinnovators.devprofilesbackend.modules.developers;

import com.dsinnovators.devprofilesbackend.modules.developers.entities.Profile;
import com.dsinnovators.devprofilesbackend.modules.stats.DevelopersSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @Query("SELECT new com.dsinnovators.devprofilesbackend.modules.stats.DevelopersSummary( " +
            "       SUM(p.totalCommitContributions), " +
            "       SUM(p.totalPullRequestContributions), " +
            "       SUM(p.totalPullRequestReviewContributions), " +
            "       SUM(p.totalIssueContributions), " +
            "       SUM(p.totalRepositoryContributions), " +
            "       SUM(p.totalRepositoriesWithContributedIssues), " +
            "       SUM(p.totalRepositoriesWithContributedCommits), " +
            "       SUM(p.totalRepositoriesWithContributedPullRequests), " +
            "       SUM(p.totalRepositoriesWithContributedPullRequestReviews)) " +
            " FROM Profile p join Developer d on d.id = p.developer.id " +
            " WHERE p.gitPlatform = 'GITHUB'")
    DevelopersSummary getAccumulatedValuesQuery();
}
