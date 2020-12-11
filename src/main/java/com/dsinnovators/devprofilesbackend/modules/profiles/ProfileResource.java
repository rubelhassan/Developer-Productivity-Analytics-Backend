package com.dsinnovators.devprofilesbackend.modules.profiles;

import com.dsinnovators.devprofilesbackend.modules.profiles.entities.Profile;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Getter
@Relation(value = "profile", collectionRelation = "profiles")
public class ProfileResource extends RepresentationModel<ProfileResource> {
    // TODO: place Profile with filtered attributes
    private final Profile profile;

    public ProfileResource(Profile profile) {
        this.profile = profile;
        add(linkTo(ProfileController.class).withRel("profiles"));
        add(linkTo(ProfileController.class).slash(profile.getId()).withSelfRel());
    }
}
