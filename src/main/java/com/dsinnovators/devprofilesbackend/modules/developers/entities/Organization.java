package com.dsinnovators.devprofilesbackend.modules.developers.entities;

import com.dsinnovators.devprofilesbackend.github.entities.GithubOrganization;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "organizations")
@Relation(value = "organization", collectionRelation = "organizations")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "organizations_id_generator")
    @SequenceGenerator(name = "organizations_id_generator", sequenceName = "organizations_id_seq", initialValue = 101)
    private Long id;

    private String githubId;
    private String login;
    private String name;
    private String email;
    private String description;
    private String url;
    private String websiteUrl;

    @JsonIgnore
    @ManyToMany(mappedBy = "organizations")
    @Builder.Default
    private List<Profile> profiles = new ArrayList<>();

    public static Organization from(GithubOrganization githubOrganization) {
        return Organization.builder()
                .githubId(githubOrganization.getGithubId())
                .login(githubOrganization.getLogin())
                .name(githubOrganization.getName())
                .email(githubOrganization.getEmail())
                .description(githubOrganization.getDescription())
                .url(githubOrganization.getUrl())
                .websiteUrl(githubOrganization.getWebsiteUrl())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Organization)) return false;
        Organization that = (Organization) o;
        return getGithubId().equals(that.getGithubId()) && getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGithubId(), getName());
    }

    @Override
    public String toString() {
        return "Organization{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", websiteUrl='" + websiteUrl + '\'' +
                '}';
    }
}
