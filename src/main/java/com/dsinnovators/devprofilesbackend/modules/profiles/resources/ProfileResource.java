package com.dsinnovators.devprofilesbackend.modules.profiles.resources;

import com.dsinnovators.devprofilesbackend.github.entities.GithubResponseData;
import com.dsinnovators.devprofilesbackend.github.entities.User;
import com.dsinnovators.devprofilesbackend.modules.profiles.ProfileController;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Getter
@Relation(value = "profile", collectionRelation = "profiles")
public class ProfileResource extends RepresentationModel<ProfileResource> {
    // TODO: place Profile with filtered attributes
    private final User profile;

    public ProfileResource(GithubResponseData data, Long id) {
        this.profile = data.getData().getUser();
        add(linkTo(ProfileController.class).withRel("profiles"));
        add(linkTo(ProfileController.class).slash(id).withSelfRel());
    }
}
