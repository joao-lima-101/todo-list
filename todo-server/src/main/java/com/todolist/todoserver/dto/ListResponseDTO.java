package com.todolist.todoserver.dto;

public record ListResponseDTO(
        Long id,
        String name,
        String description
) {
}
