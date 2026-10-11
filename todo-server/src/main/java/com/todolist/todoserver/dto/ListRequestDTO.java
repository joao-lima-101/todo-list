package com.todolist.todoserver.dto;

import jakarta.validation.constraints.NotBlank;

public record ListRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        String name,
        String description
) {}
