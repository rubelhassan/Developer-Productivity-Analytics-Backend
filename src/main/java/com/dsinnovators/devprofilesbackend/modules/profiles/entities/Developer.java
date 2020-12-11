package com.dsinnovators.devprofilesbackend.modules.profiles.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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
    @SequenceGenerator(name="developers_id_generator", sequenceName = "developers_id_seq", initialValue=101)
    private Long id;

    private String email;
    private String fullName;
    private String designation;
    private Date employmentDate;
    private String profilePictureUrl;

    @OneToMany(mappedBy = "developer")
    private List<Profile> profiles;
}
