package com.todolist.todoserver.controller;

import com.todolist.todoserver.dto.ApiResponseDTO;
import com.todolist.todoserver.dto.TaskRequestDTO;
import com.todolist.todoserver.dto.TaskResponseDTO;
import com.todolist.todoserver.entity.User;
import com.todolist.todoserver.mapper.TaskMapper;
import com.todolist.todoserver.service.TaskService;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/list/{idList}/task")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO> create(@PathVariable Long idList, @Valid @RequestBody TaskRequestDTO dto) {
        taskService.create(idList, taskMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO("Tarefa criada com sucesso"));
    }

    @PatchMapping("/{idTask}/done")
    public ResponseEntity<ApiResponseDTO> toggleDone(
            @PathVariable Long idList,
            @PathVariable Long idTask,
            @AuthenticationPrincipal User user
    ) {
        taskService.toggleDone(idList, idTask, user);
        return ResponseEntity.ok(new ApiResponseDTO("Tarefa atualizada com sucesso"));
    }

    @PatchMapping("/{idTask}")
    public ResponseEntity<ApiResponseDTO> update(
            @PathVariable Long idList,
            @PathVariable Long idTask,
            @Valid @RequestBody TaskRequestDTO dto,
            @AuthenticationPrincipal User user
    ) {
        taskService.update(idList, idTask, taskMapper.toEntity(dto), user);
        return ResponseEntity.ok(new ApiResponseDTO("Tarefa atualizada com sucesso"));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAll(
            @PathVariable Long idList, @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(taskService.getAll(idList, user));
    }

    @DeleteMapping("/{idTask}")
    public ResponseEntity<Void> delete(
            @PathVariable Long idList,
            @PathVariable Long idTask,
            @AuthenticationPrincipal User user
    ) {
        taskService.remove(idList, idTask, user);
        return ResponseEntity.noContent().build();
    }

}
