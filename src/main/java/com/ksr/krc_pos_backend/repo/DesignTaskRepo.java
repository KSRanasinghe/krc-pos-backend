package com.ksr.krc_pos_backend.repo;

import com.ksr.krc_pos_backend.model.DesignTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DesignTaskRepo extends JpaRepository<DesignTask, Integer> {
    Optional<DesignTask> findByUuid(UUID uuid);
}
