package com.dsinnovators.devprofilesbackend.modules.profiles;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value="profiles", produces = "application/json")
@AllArgsConstructor
public class ProfileController {
    private ProfileService profileService;

    @GetMapping("/{id}")
    ResponseEntity<ProfileResource> getUserProfile(@PathVariable("id") Long id) throws JsonProcessingException {
        return ok(new ProfileResource(profileService.getProfile(id)));
    }
}
