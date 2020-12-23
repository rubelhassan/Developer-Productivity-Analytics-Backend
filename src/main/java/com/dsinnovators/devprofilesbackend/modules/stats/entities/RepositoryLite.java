package com.dsinnovators.devprofilesbackend.modules.stats.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class RepositoryLite {
    @JsonIgnore
    private String githubId;
    private String name;
    private String url;
    private int stars;
    private int forks;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RepositoryLite)) return false;
        RepositoryLite that = (RepositoryLite) o;
        return getGithubId().equals(that.getGithubId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGithubId());
    }
}
