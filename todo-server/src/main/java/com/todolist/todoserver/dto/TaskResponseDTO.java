package com.todolist.todoserver.dto;

import com.todolist.todoserver.entity.Task;
import com.todolist.todoserver.enums.TaskPriority;

import java.time.LocalDate;

public record TaskResponseDTO(
        Long id,
        String name,
        String description,
        LocalDate dueDate,
        TaskPriority priority,
        boolean done
) {
    public TaskResponseDTO(Task task) {
        this(task.getId(), task.getName(), task.getDescription(), task.getDueDate(), task.getPriority(), task.getDone());
    }
}

