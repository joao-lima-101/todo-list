package com.todolist.todoserver.dto;

import com.todolist.todoserver.enums.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TaskRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        String name,
        String description,
        TaskPriority priority,
        LocalDate dueDate
) {}
