package com.todolist.todoserver.mapper;

import com.todolist.todoserver.dto.TaskRequestDTO;
import com.todolist.todoserver.entity.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public Task toEntity(TaskRequestDTO dto) {
        Task task = new Task();

        task.setName(dto.name());
        task.setDescription(dto.description());
        task.setPriority(dto.priority());
        task.setDueDate(dto.dueDate());
        task.setDone(false);

        return task;
    }
}
