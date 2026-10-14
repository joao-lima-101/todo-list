package com.todolist.todoserver.dto;

import com.todolist.todoserver.entity.Tasklist;

public record ListResponseDTO(
        Long id,
        String name,
        String description
) {
    public ListResponseDTO(Tasklist list) {
        this(list.getId(), list.getName(), list.getDescription());
    }
}
