package com.todolist.todoserver.controller;

import com.todolist.todoserver.dto.ApiResponseDTO;
import com.todolist.todoserver.dto.ListRequestDTO;
import com.todolist.todoserver.dto.ListResponseDTO;
import com.todolist.todoserver.entity.User;
import com.todolist.todoserver.service.TasklistService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/list")
public class TasklistController {

    private final TasklistService tasklistService;

    public TasklistController(TasklistService tasklistService) {
        this.tasklistService = tasklistService;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO> create(
            @Valid @RequestBody ListRequestDTO dto,
            @AuthenticationPrincipal User user
    ) {
        tasklistService.create(user, dto.name(), dto.description());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO("Nova lista criada com sucesso!"));
    }

    @GetMapping("/{idList}")
    public ResponseEntity<ListResponseDTO> get(@PathVariable Long idList, @AuthenticationPrincipal User user) {
        ListResponseDTO dto = tasklistService.get(idList, user);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{idList}")
    public ResponseEntity<ApiResponseDTO> update(
            @PathVariable Long idList,
            @RequestBody @Valid ListRequestDTO dto,
            @AuthenticationPrincipal User user
    ) {
        tasklistService.update(idList, dto.name(), dto.description(), user);
        return ResponseEntity.ok(new ApiResponseDTO("Lista atualizada com sucesso!"));
    }

    @DeleteMapping("/{idList}")
    public ResponseEntity<Void> delete(@PathVariable Long idList, @AuthenticationPrincipal User user) {
        tasklistService.delete(idList, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ListResponseDTO>> getLists( @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(tasklistService.getAll(user));
    }
}
