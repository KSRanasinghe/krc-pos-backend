package com.ksr.krc_pos_backend.repo;

import com.ksr.krc_pos_backend.model.TaskVariants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskVariantsRepo extends JpaRepository<TaskVariants, Integer> {
}
