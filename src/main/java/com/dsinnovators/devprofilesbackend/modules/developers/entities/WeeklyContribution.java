package com.dsinnovators.devprofilesbackend.modules.developers.entities;

import com.dsinnovators.devprofilesbackend.github.entities.GithubContributionSummary;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.server.core.Relation;

import javax.persistence.*;
import java.util.Date;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "weekly_contributions")
@Relation(value = "weeklyContribution", collectionRelation = "weeklyContributions")
public class WeeklyContribution {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contrib_id_generator")
    @SequenceGenerator(name = "contrib_id_generator", sequenceName = "contrib_id_seq", initialValue = 101)
    private Long id;
    private boolean hasAnyContributions;
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

    @JsonIgnore
    @Column(name = "contribution_calendar_json", columnDefinition = "TEXT")
    private String contributionCalendar;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Transient
    private JsonNode contributionsCalendar;

    @PostLoad
    @PostPersist
    public void loadContributionCalender() {
        if(!isEmpty(contributionCalendar)) {
            try {
                contributionsCalendar = new ObjectMapper().readTree(contributionCalendar);
            } catch (JsonProcessingException e) {
                log.error("Could not parse contribution calender: " + contributionCalendar);
            }
        }
    }

    public static WeeklyContribution from(GithubContributionSummary contributionSummary,
                                          WeeklyContribution weeklyContributions) {
        WeeklyContributionBuilder builder =
                WeeklyContribution.builder()
                                  .hasAnyContributions(contributionSummary.isHasAnyContributions())
                                  .hasAnyRestrictedContributions(contributionSummary.isHasAnyRestrictedContributions())
                                  .totalCommitContributions(contributionSummary.getTotalCommitContributions())
                                  .totalPullRequestContributions(contributionSummary.getTotalPullRequestContributions())
                                  .totalPullRequestReviewContributions(
                                          contributionSummary.getTotalPullRequestReviewContributions())
                                  .totalIssueContributions(contributionSummary.getTotalIssueContributions())
                                  .totalRepositoryContributions(contributionSummary.getTotalRepositoryContributions())
                                  .totalRepositoriesWithContributedIssues(
                                          contributionSummary.getTotalRepositoriesWithContributedIssues())
                                  .totalRepositoriesWithContributedCommits(
                                          contributionSummary.getTotalRepositoriesWithContributedCommits())
                                  .totalRepositoriesWithContributedPullRequests(
                                          contributionSummary.getTotalRepositoriesWithContributedPullRequests())
                                  .totalRepositoriesWithContributedPullRequestReviews(
                                          contributionSummary.getTotalRepositoriesWithContributedPullRequestReviews())
                                  .restrictedContributionsCount(contributionSummary.getRestrictedContributionsCount())
                                  .startedAt(contributionSummary.getStartedAt())
                                  .endedAt(contributionSummary.getEndedAt())
                                  .contributionCalendar(contributionSummary.getContributionCalenderString())
                                  .contributionsCalendar(contributionSummary.getContributionCalendar());
        if (weeklyContributions != null) {
            builder.id(weeklyContributions.getId());
        }
        return builder.build();
    }

    @Override
    public String toString() {
        return "WeeklyContribution{" +
                "id=" + id +
                ", hasAnyContributions=" + hasAnyContributions +
                ", hasAnyRestrictedContributions=" + hasAnyRestrictedContributions +
                ", totalCommitContributions=" + totalCommitContributions +
                ", totalPullRequestContributions=" + totalPullRequestContributions +
                ", totalPullRequestReviewContributions=" + totalPullRequestReviewContributions +
                ", totalIssueContributions=" + totalIssueContributions +
                ", totalRepositoryContributions=" + totalRepositoryContributions +
                ", totalRepositoriesWithContributedIssues=" + totalRepositoriesWithContributedIssues +
                ", totalRepositoriesWithContributedCommits=" + totalRepositoriesWithContributedCommits +
                ", totalRepositoriesWithContributedPullRequests=" + totalRepositoriesWithContributedPullRequests +
                ", totalRepositoriesWithContributedPullRequestReviews=" + totalRepositoriesWithContributedPullRequestReviews +
                ", restrictedContributionsCount=" + restrictedContributionsCount +
                ", startedAt=" + startedAt +
                ", endedAt=" + endedAt +
                ", contributionCalendar='" + contributionCalendar + '\'' +
                ", contributionsCalendar=" + contributionsCalendar +
                '}';
    }
}
