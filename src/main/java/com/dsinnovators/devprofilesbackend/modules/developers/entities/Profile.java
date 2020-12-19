package com.dsinnovators.devprofilesbackend.modules.developers.entities;

import com.dsinnovators.devprofilesbackend.github.entities.GithubRepository;
import com.dsinnovators.devprofilesbackend.github.entities.GithubUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import javax.persistence.*;
import java.util.*;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.springframework.util.CollectionUtils.isEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "profiles")
@Relation(value = "profile", collectionRelation = "profiles")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profiles_id_generator")
    @SequenceGenerator(name = "profiles_id_generator", sequenceName = "profiles_id_seq", initialValue = 101)
    private Long id;

    private String name;
    private String login;
    private String email;
    private String bio;
    private String avatarUrl;
    private String websiteUrl;
    private String company;
    private String location;
    private Date createdAt;
    private Integer followers;
    private Integer following;
    private Integer starredRepositories;
    private Integer repositoriesContributedTo;
    private Integer gists;
    private Integer issues;

    @Builder.Default
    private Integer totalRepositories = 0;

    @Builder.Default
    private Integer totalStars = 0;

    @Builder.Default
    private Integer totalRepositoriesContributedTo = 0;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "profile_org",
            joinColumns = {@JoinColumn(name = "profile_id")},
            inverseJoinColumns = {@JoinColumn(name = "organization_id")}
    )
    @Builder.Default
    private List<Organization> organizations = new ArrayList<>();

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Repository> repositories = new ArrayList<>();

    // extracted contributions summary
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
    private Date contribStartedAt;
    private Date contribEndedAt;

    @OneToOne(cascade = CascadeType.MERGE)
    private WeeklyContribution weeklyContributions;

    @Enumerated(EnumType.STRING)
    private GitPlatform gitPlatform = GitPlatform.GITHUB;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "developer_id", nullable = false)
    private Developer developer;

    @JsonIgnore
    @Transient
    @Builder.Default
    private List<GithubRepository> pinnedRepositories = new ArrayList<>();

    @JsonIgnore
    @Transient
    @Builder.Default
    private List<GithubRepository> topRepositories = new ArrayList<>();

    @JsonIgnore
    @Transient
    @Builder.Default
    private Map<String, Organization> organizationMap = new HashMap<>();

    public void setWeeklyContributions(WeeklyContribution weeklyContributions) {
        this.weeklyContributions = weeklyContributions;
        if (weeklyContributions != null) {
            weeklyContributions.setProfile(this);
        }
    }

    @PostLoad
    @PostPersist
    public void loadOrganizations() {
        if (!isEmpty(this.organizations)) {
            this.organizationMap = this.organizations.stream()
                                                     .collect(toMap(Organization::getGithubId, identity()));
        }
    }

    public static Profile from(GithubUser user) {
        return Profile.builder()
                      .name(user.getName())
                      .login(user.getLogin())
                      .email(user.getEmail())
                      .bio(user.getBio())
                      .avatarUrl(user.getAvatarUrl())
                      .websiteUrl(user.getWebsiteUrl())
                      .company(user.getCompany())
                      .location(user.getLocation())
                      .createdAt(user.getCreatedAt())
                      .followers(user.getFollowers().getTotalCount())
                      .following(user.getFollowing().getTotalCount())
                      .starredRepositories(user.getStarredRepositories().getTotalCount())
                      .repositoriesContributedTo(user.getRepositoriesContributedTo().getTotalCount())
                      .gists(user.getGists().getTotalCount())
                      .issues(user.getIssues().getTotalCount())
                      .hasAnyContributions(user.getContributionsSummary().isHasAnyContributions())
                      .hasAnyRestrictedContributions(user.getContributionsSummary().isHasAnyRestrictedContributions())
                      .totalCommitContributions(user.getContributionsSummary().getTotalCommitContributions())
                      .totalPullRequestContributions(user.getContributionsSummary().getTotalPullRequestContributions())
                      .totalPullRequestReviewContributions(
                              user.getContributionsSummary().getTotalPullRequestReviewContributions())
                      .totalIssueContributions(user.getContributionsSummary().getTotalIssueContributions())
                      .totalRepositoryContributions(user.getContributionsSummary().getTotalRepositoryContributions())
                      .totalRepositoriesWithContributedIssues(
                              user.getContributionsSummary().getTotalRepositoriesWithContributedIssues())
                      .totalRepositoriesWithContributedCommits(
                              user.getContributionsSummary().getTotalRepositoriesWithContributedCommits())
                      .totalRepositoriesWithContributedPullRequests(
                              user.getContributionsSummary().getTotalRepositoriesWithContributedPullRequests())
                      .totalRepositoriesWithContributedPullRequestReviews(
                              user.getContributionsSummary().getTotalRepositoriesWithContributedPullRequestReviews())
                      .restrictedContributionsCount(user.getContributionsSummary().getRestrictedContributionsCount())
                      .contribStartedAt(user.getContributionsSummary().getStartedAt())
                      .contribEndedAt(user.getContributionsSummary().getEndedAt())
                      .gitPlatform(GitPlatform.GITHUB)
                      .build();
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", bio='" + bio + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", websiteUrl='" + websiteUrl + '\'' +
                ", company='" + company + '\'' +
                ", location='" + location + '\'' +
                ", createdAt=" + createdAt +
                ", followers=" + followers +
                ", following=" + following +
                ", starredRepositories=" + starredRepositories +
                ", repositoriesContributedTo=" + repositoriesContributedTo +
                ", gists=" + gists +
                ", issues=" + issues +
                ", organizations=" + organizations +
                ", repositories=" + repositories +
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
                ", contribStartedAt=" + contribStartedAt +
                ", contribEndedAt=" + contribEndedAt +
                ", weeklyContributions=" + weeklyContributions +
                ", gitPlatform=" + gitPlatform +
                '}';
    }
}
