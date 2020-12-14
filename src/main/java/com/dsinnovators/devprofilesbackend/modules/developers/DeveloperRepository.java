package com.dsinnovators.devprofilesbackend.modules.developers;

import com.dsinnovators.devprofilesbackend.modules.developers.entities.Developer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeveloperRepository extends JpaRepository<Developer, Long> {
}
