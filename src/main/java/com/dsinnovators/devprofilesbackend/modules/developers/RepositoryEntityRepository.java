package com.dsinnovators.devprofilesbackend.modules.developers;

import com.dsinnovators.devprofilesbackend.modules.developers.entities.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryEntityRepository extends JpaRepository<Repository, Long> {
}
