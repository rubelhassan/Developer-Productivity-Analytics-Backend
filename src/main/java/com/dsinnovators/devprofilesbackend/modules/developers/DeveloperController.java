package com.dsinnovators.devprofilesbackend.modules.developers;

import com.dsinnovators.devprofilesbackend.utils.DeveloperNotFoundException;
import com.dsinnovators.devprofilesbackend.utils.GithubUserNotFound;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
