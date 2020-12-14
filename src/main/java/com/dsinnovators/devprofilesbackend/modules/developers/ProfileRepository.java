package com.dsinnovators.devprofilesbackend.modules.developers;

import com.dsinnovators.devprofilesbackend.modules.developers.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
