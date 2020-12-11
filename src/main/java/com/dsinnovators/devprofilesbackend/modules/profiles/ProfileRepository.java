package com.dsinnovators.devprofilesbackend.modules.profiles;

import com.dsinnovators.devprofilesbackend.modules.profiles.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
