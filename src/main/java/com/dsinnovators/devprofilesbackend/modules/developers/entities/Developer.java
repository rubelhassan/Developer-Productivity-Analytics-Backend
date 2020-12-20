package com.dsinnovators.devprofilesbackend.modules.developers.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.dsinnovators.devprofilesbackend.modules.developers.entities.GitPlatform.GITHUB;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "developers")
@Relation(value = "developer", collectionRelation = "developers")
public class Developer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "developers_id_generator")
    @SequenceGenerator(name = "developers_id_generator", sequenceName = "developers_id_seq", initialValue = 101)
    private Long id;

    @NotEmpty
    @Email
    private String email;

    private String fullName;
    private String designation;
    private Date employmentDate;
    private String profilePictureUrl;

    @JsonIgnore
    private String githubAccessToken;

    @OneToMany(mappedBy = "developer", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Profile> profiles = new ArrayList<>();

    public void addProfile(Profile profile) {
        if (profile == null) return;
        if (profiles == null) {
            profiles = new ArrayList<>();
        }
        this.profiles.add(profile);
        profile.setDeveloper(this);
    }

    public Profile githubProfile() {
        if (profiles.isEmpty()) {
            return null;
        }

        return profiles.stream()
                       .filter(profile -> profile.getGitPlatform() == GITHUB)
                       .findFirst()
                       .orElse(null);
    }
}
