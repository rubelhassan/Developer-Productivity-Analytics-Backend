package com.dsinnovators.devprofilesbackend.modules.developers;

import com.dsinnovators.devprofilesbackend.modules.developers.entities.Developer;
import com.dsinnovators.devprofilesbackend.modules.developers.entities.GithubCode;
import com.dsinnovators.devprofilesbackend.utils.DeveloperNotFoundException;
import com.dsinnovators.devprofilesbackend.utils.GithubUserNotFound;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value="developers", produces = "application/hal+json")
@AllArgsConstructor
public class DeveloperController {
    private DeveloperService developerService;

    @GetMapping("/{id}")
    ResponseEntity<DeveloperResource> getDeveloper(@PathVariable("id") Long id) throws DeveloperNotFoundException, GithubUserNotFound {
        return ok(new DeveloperResource(developerService.getProfile(id)));
    }

    @PostMapping
    ResponseEntity<DeveloperResource> createDeveloper(@RequestBody @Valid Developer developer){
        developer = developerService.createProfile(developer);
        return ResponseEntity.created(linkTo(DeveloperController.class).slash(developer.getId()).toUri())
                .body(new DeveloperResource(developer));
    }

    @PostMapping("/{id}/github/code")
    ResponseEntity<DeveloperResource> updateGithubToken(@PathVariable("id") Long id,
                                                        @RequestBody @Valid GithubCode code) throws DeveloperNotFoundException, GithubUserNotFound {
        return ok(new DeveloperResource(developerService.updateToken(id, code.getCode())));
    }
}
