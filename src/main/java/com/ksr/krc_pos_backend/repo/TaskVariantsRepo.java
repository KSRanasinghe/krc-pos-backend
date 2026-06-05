package com.ksr.krc_pos_backend.repo;

import com.ksr.krc_pos_backend.model.TaskVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskVariantsRepo extends JpaRepository<TaskVariant, Integer> {
    Optional<TaskVariant> findByUuid(UUID uuid);

    List<TaskVariant> findByDesignTaskUuid(UUID uuid);
}
