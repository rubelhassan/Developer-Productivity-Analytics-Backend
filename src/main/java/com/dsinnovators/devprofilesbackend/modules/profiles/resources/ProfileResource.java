package com.dsinnovators.devprofilesbackend.modules.profiles.resources;

import com.dsinnovators.devprofilesbackend.modules.profiles.ProfileController;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Getter
@Relation(value = "profile", collectionRelation = "profiles")
public class ProfileResource extends RepresentationModel<ProfileResource> {
    // TODO: place Profile with filtered attributes
    private final String profile;

    public ProfileResource(String profile, Long id) {
        this.profile = profile;
        add(linkTo(ProfileController.class).withRel("profiles"));
        add(linkTo(ProfileController.class).slash(id).withSelfRel());
    }
}
