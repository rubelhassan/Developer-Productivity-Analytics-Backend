package com.dsinnovators.devprofilesbackend.modules.profiles.entities;

import com.dsinnovators.devprofilesbackend.github.entities.GithubRepository;
import com.dsinnovators.devprofilesbackend.github.entities.GithubResponseData;
import com.dsinnovators.devprofilesbackend.github.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    @SequenceGenerator(name="profiles_id_generator", sequenceName = "profiles_id_seq", initialValue=101)
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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Organization> organizations = new ArrayList<>();

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Repository> repositories = new ArrayList<>();

    // extracted contributions summary
    private Integer totalCommitContributions;
    private Integer totalRepositoryContributions;
    private Integer totalRepositoriesWithContributedIssues;
    // try JSONB
    private String weeklyContributionsJson;

    @Enumerated(EnumType.STRING)
    private GitPlatform gitPlatform = GitPlatform.GITHUB;

    @ManyToOne
    @JoinColumn(name="developer_id", nullable = true) // TODO: make nullable false
    private Developer developer;

    @Transient
    private List<GithubRepository> pinnedRepositories = new ArrayList<>();

    @Transient
    private List<GithubRepository> topRepositories = new ArrayList<>();

    public static enum GitPlatform{
        GITHUB, BITBUCKET, GITLAB
    }

    public static Profile from(GithubResponseData data) {
        User user = data.getData().getUser();
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
                .organizations(user.getOrganizationConnections().getOrganizations().stream()
                        .map(Organization::from)
                        .collect(Collectors.toList()))
                // TODO: set pinned and top repositories and check total count
                .repositories(user.getRepositories().getRepositories().stream()
                        .map(Repository::from)
                        .collect(Collectors.toList()))
                .totalCommitContributions(user.getAllContributions().get("totalCommitContributions").asInt(0))
                .totalRepositoryContributions(user.getAllContributions().get("totalRepositoryContributions").asInt(0))
                .totalRepositoriesWithContributedIssues(user.getAllContributions().get("totalRepositoriesWithContributedIssues").asInt(0))
                // TODO: populate weeklyContributionsJson
            .build();
    }
}
