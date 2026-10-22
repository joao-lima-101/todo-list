package com.todolist.todoserver.dto;

public record ListProgressDTO(
        Long listId,
        Long totalTasks,
        Long completedTasks
) {}
