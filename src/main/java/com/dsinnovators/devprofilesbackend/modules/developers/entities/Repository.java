package com.dsinnovators.devprofilesbackend.modules.developers.entities;

import com.dsinnovators.devprofilesbackend.github.entities.GithubRepository;
import com.dsinnovators.devprofilesbackend.github.entities.Language;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.server.core.Relation;

import javax.persistence.*;
import java.util.Objects;

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
    @SequenceGenerator(name = "repositories_id_generator", sequenceName = "repositories_id_seq", initialValue = 101)
    private Long id;

    private String repositoryId;
    private String name;
    private String description;
    private String url;
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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Repository)) return false;
        Repository that = (Repository) o;
        return getId().equals(that.getId()) && getRepositoryId().equals(that.getRepositoryId()) &&
                getName().equals(that.getName()) && Objects.equals(getOwner(), that.getOwner());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getRepositoryId(), getName(), getOwner());
    }

    public static Repository from(GithubRepository githubRepository) {
        String topic = null;
        if (githubRepository.getRepositoryTopics().getNodes().size() > 0) {
            topic = githubRepository.getRepositoryTopics().getNodes().get(0).getTopic().getName();
        }

        return Repository.builder()
                         .repositoryId(githubRepository.getRepositoryId())
                         .name(githubRepository.getName())
                         .description(githubRepository.getDescription())
                         .url(githubRepository.getUrl())
                         .stargazerCount(githubRepository.getStargazerCount())
                         .nameWithOwner(githubRepository.getNameWithOwner())
                         .isPrivate(githubRepository.isPrivate())
                         .isFork(githubRepository.isFork())
                         .forkCount(githubRepository.getForkCount())
                         .owner(githubRepository.getOwner().getLogin())
                         .primaryLanguage(
                                 ofNullable(githubRepository.getPrimaryLanguage())
                                         .map(Language::getName).orElse(null))
                         .topic(topic)
                         .pullRequests(githubRepository.getPullRequests().getTotalCount())
                         .issues(githubRepository.getIssues().getTotalCount())
                         .watchers(githubRepository.getWatchers().getTotalCount())
                         .isPinned(false)
                         .isTop(false)
                         .build();
    }

    @Override
    public String toString() {
        return "Repository{" +
                "id=" + id +
                ", repositoryId='" + repositoryId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", stargazerCount=" + stargazerCount +
                ", nameWithOwner='" + nameWithOwner + '\'' +
                ", isPrivate=" + isPrivate +
                ", isFork=" + isFork +
                ", forkCount=" + forkCount +
                ", owner='" + owner + '\'' +
                ", primaryLanguage='" + primaryLanguage + '\'' +
                ", topic='" + topic + '\'' +
                ", pullRequests=" + pullRequests +
                ", issues=" + issues +
                ", watchers=" + watchers +
                ", isPinned=" + isPinned +
                ", isTop=" + isTop +
                '}';
    }
}
