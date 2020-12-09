package com.dsinnovators.devprofilesbackend.modules.profiles.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import javax.persistence.*;


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

    @ManyToOne
    @JoinColumn(name="developer_id", nullable = false)
    private Developer developer;

    private String authToken;
    private String email;
    private Integer commitContributions;
    private Integer repositoryContributions;
    private Integer prContributions;
    private Integer followers;
    private Integer following;
    private Integer stars;
    private Integer starredRepositories;
    private Integer organization;

    // TODO: try jsonb if efficient
    private String contributionSummary;
    private String contributionWeekly;

    @Enumerated(EnumType.STRING)
    private GitPlatform gitPlatform;

    public static enum GitPlatform{
        GITHUB, BITBUCKET, GITLAB
    }
}
