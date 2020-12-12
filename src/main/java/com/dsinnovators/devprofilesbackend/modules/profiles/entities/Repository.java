package com.dsinnovators.devprofilesbackend.modules.profiles.entities;

import com.dsinnovators.devprofilesbackend.github.entities.GithubRepository;
import com.dsinnovators.devprofilesbackend.github.entities.Language;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.server.core.Relation;

import javax.persistence.*;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "repositories")
@Relation(value = "repository", collectionRelation = "repositories")
public class Repository {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "repositories_id_generator")
    @SequenceGenerator(name="repositories_id_generator", sequenceName = "repositories_id_seq", initialValue=101)
    private Long id;

    private String name;
    private String description;
    private Integer stargazerCount;
    private String nameWithOwner;
    private boolean isPrivate;
    private boolean isFork;
    private int forkCount;
    private String owner;
    private String primaryLanguage;
    private String topic;
    private Integer pullRequests;
    private Integer issues;
    private Integer watchers;
    private boolean isPinned;
    private boolean isTop;

    @ManyToOne
    @JoinColumn(name="profile_id", nullable = true) // TODO: make nullable false
    private Profile profile;

    public static Repository from(GithubRepository githubRepository) {
        String topic = null;
        if (githubRepository.getRepositoryTopics().getNodes().size() > 0) {
            topic = githubRepository.getRepositoryTopics().getNodes().get(0).getTopic().getName();
        }

        return Repository.builder()
                .name(githubRepository.getName())
                .description(githubRepository.getDescription())
                .stargazerCount(githubRepository.getStargazerCount())
                .nameWithOwner(githubRepository.getNameWithOwner())
                .isPrivate(githubRepository.isPrivate())
                .isFork(githubRepository.isFork())
                .forkCount(githubRepository.getForkCount())
                .owner(githubRepository.getOwner().getLogin())
                .primaryLanguage(ofNullable(githubRepository.getPrimaryLanguage()).map(Language::getName).orElse(null))
                .topic(topic)
                .pullRequests(githubRepository.getPullRequests().getTotalCount())
                .issues(githubRepository.getIssues().getTotalCount())
                .watchers(githubRepository.getWatchers().getTotalCount())
                .isPinned(false)
                .isTop(false)
                .build();
    }
}
