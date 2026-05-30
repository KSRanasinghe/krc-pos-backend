package com.ksr.krc_pos_backend.service;

import com.ksr.krc_pos_backend.dto.DesignTaskDto;
import com.ksr.krc_pos_backend.dto.TaskVariantDto;
import com.ksr.krc_pos_backend.model.DesignTask;
import com.ksr.krc_pos_backend.model.TaskVariant;
import com.ksr.krc_pos_backend.repo.DesignTaskRepo;
import com.ksr.krc_pos_backend.repo.TaskVariantsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DesignTaskService {

    private final DesignTaskRepo designTaskRepo;
    private final TaskVariantsRepo taskVariantsRepo;

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

    public List<TaskVariantDto> findByDesignTaskUuid(UUID uuid) {
        DesignTask designTask = designTaskRepo.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("No task found"));

        return taskVariantsRepo.findByDesignTaskUuid(designTask.getUuid())
                .stream()
                .map(variant -> TaskVariantDto.builder()
                        .uuid(variant.getUuid())
                        .label(variant.getLabel())
                        .price(variant.getPrice())
                        .isActive(variant.isActive())
                        .build())
                .collect(Collectors.toList());
    }

    public TaskVariantDto addVariant(TaskVariantDto taskVariantDto) {
        DesignTask designTask = designTaskRepo.findByUuid(taskVariantDto.getTaskUuid())
                .orElseThrow(() -> new RuntimeException("No task found"));

        TaskVariant taskVariant = TaskVariant.builder()
                .label(taskVariantDto.getLabel())
                .price(taskVariantDto.getPrice())
                .designTask(designTask)
                .build();

        TaskVariant savedVariant = taskVariantsRepo.save(taskVariant);

        return TaskVariantDto.builder()
                .uuid(savedVariant.getUuid())
                .label(savedVariant.getLabel())
                .price(savedVariant.getPrice())
                .isActive(savedVariant.isActive())
                .taskUuid(savedVariant.getDesignTask().getUuid())
                .build();

    }

    public TaskVariantDto updateVariantState(UUID uuid) {
        TaskVariant taskVariant = taskVariantsRepo.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("No task variant found"));

        taskVariant.setActive(!taskVariant.isActive());

        TaskVariant savedVariant = taskVariantsRepo.save(taskVariant);

        return TaskVariantDto.builder()
                .uuid(savedVariant.getUuid())
                .label(savedVariant.getLabel())
                .price(savedVariant.getPrice())
                .isActive(savedVariant.isActive())
                .taskUuid(savedVariant.getDesignTask().getUuid())
                .build();
    }

    public TaskVariantDto updateVariant(UUID uuid, TaskVariantDto taskVariantDto) {
        TaskVariant taskVariant = taskVariantsRepo.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("No task variant found"));

        taskVariant.setLabel(taskVariantDto.getLabel());
        taskVariant.setPrice(taskVariantDto.getPrice());

        TaskVariant savedVariant = taskVariantsRepo.save(taskVariant);

        return TaskVariantDto.builder()
                .uuid(savedVariant.getUuid())
                .label(savedVariant.getLabel())
                .price(savedVariant.getPrice())
                .isActive(savedVariant.isActive())
                .taskUuid(savedVariant.getDesignTask().getUuid())
                .build();
    }
}
