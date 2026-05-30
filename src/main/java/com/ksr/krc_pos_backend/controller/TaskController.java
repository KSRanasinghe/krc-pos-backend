package com.ksr.krc_pos_backend.controller;

import com.ksr.krc_pos_backend.dto.DesignTaskDto;
import com.ksr.krc_pos_backend.service.DesignTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final DesignTaskService taskService;

    @GetMapping("/")
    public ResponseEntity<List<DesignTaskDto>>  getAllTasks() {
        return new ResponseEntity<>(taskService.getAllTasks(), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<DesignTaskDto> addTask(@RequestBody DesignTaskDto designTaskDto) {
        return new ResponseEntity<>(taskService.addTask(designTaskDto), HttpStatus.CREATED);
    }

    @PutMapping("/{uuid}/state")
    public ResponseEntity<DesignTaskDto> updateTaskState(@PathVariable UUID uuid){
        return new ResponseEntity<>(taskService.updateTaskState(uuid), HttpStatus.OK);
    }
}
