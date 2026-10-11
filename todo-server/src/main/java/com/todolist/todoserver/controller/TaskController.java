package com.todolist.todoserver.controller;

import com.todolist.todoserver.dto.ApiResponseDTO;
import com.todolist.todoserver.dto.TaskRequestDTO;
import com.todolist.todoserver.dto.TaskResponseDTO;
import com.todolist.todoserver.service.TaskService;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/list/{idList}/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO> create(@PathVariable Long idList, @NonNull @Valid @RequestBody TaskRequestDTO dto) {
        taskService.create(idList, dto.name(), dto.description(), dto.priority(), dto.dueDate());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO("Tarefa criada com sucesso"));
    }

    @PatchMapping("/{idTask}/done")
    public ResponseEntity<ApiResponseDTO> updateDone(@PathVariable Long idList, @PathVariable Long idTask) {
        taskService.toggleDone(idList, idTask);
        return ResponseEntity.ok(new ApiResponseDTO("Tarefa atualizada com sucesso"));
    }

    @PatchMapping("/{idTask}")
    public ResponseEntity<ApiResponseDTO> update(
            @RequestBody TaskRequestDTO dto,
            @PathVariable Long idList,
            @PathVariable Long idTask
    ) {
        taskService.update(idList, idTask, dto.name(), dto.description(), dto.priority(), dto.dueDate());
        return ResponseEntity.ok(new ApiResponseDTO("Tarefa atualizada com sucesso"));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getTask(@PathVariable Long idList) {
        return ResponseEntity.ok(taskService.searchAll(idList));
    }

    @DeleteMapping("/{idTask}")
    public ResponseEntity<Void> delete(@PathVariable Long idList, @PathVariable Long idTask) {
        taskService.remove(idList, idTask);
        return ResponseEntity.noContent().build();
    }

}
