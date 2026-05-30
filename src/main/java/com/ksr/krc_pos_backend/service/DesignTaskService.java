package com.ksr.krc_pos_backend.service;

import com.ksr.krc_pos_backend.dto.DesignTaskDto;
import com.ksr.krc_pos_backend.model.DesignTask;
import com.ksr.krc_pos_backend.repo.DesignTaskRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DesignTaskService {

    private final DesignTaskRepo designTaskRepo;

    public List<DesignTaskDto> getAllTasks() {
        return designTaskRepo.findAll()
                .stream()
                .map(task -> DesignTaskDto.builder()
                        .uuid(task.getUuid())
                        .name(task.getName())
                        .isActive(task.isActive())
                        .build())
                .collect(Collectors.toList());
    }

    public DesignTaskDto addTask(DesignTaskDto designTaskDto) {
        DesignTask task = DesignTask.builder()
                .name(designTaskDto.getName())
                .isActive(true)
                .build();

        DesignTask savedTask = designTaskRepo.save(task);

        return DesignTaskDto.builder()
                .uuid(savedTask.getUuid())
                .name(savedTask.getName())
                .isActive(savedTask.isActive())
                .build();

    }

    public DesignTaskDto updateTaskState(UUID uuid) {
        DesignTask designTask = designTaskRepo.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("No task found"));

        designTask.setActive(!designTask.isActive());

        designTaskRepo.save(designTask);

        return DesignTaskDto.builder()
                .uuid(designTask.getUuid())
                .name(designTask.getName())
                .isActive(designTask.isActive())
                .build();
    }
}
