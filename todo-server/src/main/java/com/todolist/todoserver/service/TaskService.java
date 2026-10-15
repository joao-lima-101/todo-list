package com.todolist.todoserver.service;

import com.todolist.todoserver.dto.TaskResponseDTO;
import com.todolist.todoserver.entity.Task;
import com.todolist.todoserver.entity.Tasklist;
import com.todolist.todoserver.infra.errors.ResourceNotFoundException;
import com.todolist.todoserver.repository.TaskRepository;
import com.todolist.todoserver.repository.TasklistRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TasklistRepository taskListRepository;
    private final TaskRepository taskRepository;

    public TaskService(TasklistRepository taskListRepository, TaskRepository taskRepository) {
        this.taskListRepository = taskListRepository;
        this.taskRepository = taskRepository;
    }

    @Transactional
    public void create(Long idList, Task task) {
        Tasklist list = taskListRepository
                .findById(idList)
                .orElseThrow(() -> new ResourceNotFoundException("Lista não encontrada"));

        list.addTask(task);

        taskRepository.save(task);
    }

    @Transactional
    public void toggleDone(Long idList, Long idTask) {
        Task task = taskRepository.findByIdAndTasklist_Id(idTask, idList).orElseThrow(
                () -> new ResourceNotFoundException("Tarefa não encontrada")
        );

        task.setDone(!task.getDone());
    }

    @Transactional
    public void update(Long idList, Long idTask, Task task) {
        Task newTask = taskRepository.findByIdAndTasklist_Id(idTask, idList).orElseThrow(
                () -> new ResourceNotFoundException("Tarefa não encontrada")
        );

        newTask.setName(task.getName());
        newTask.setDescription(task.getDescription());
        newTask.setPriority(task.getPriority());
        newTask.setDueDate(task.getDueDate());
    }

    public List<TaskResponseDTO> searchAll(Long idList) {
        return taskRepository.findByTasklist_Id(idList)
                .stream()
                .map(TaskResponseDTO::new)
                .toList();
    }

    @Transactional
    public void remove(Long idList, Long idTask) {
        Tasklist list = taskListRepository
                .findById(idList)
                .orElseThrow(() -> new ResourceNotFoundException("Lista não encontrada"));

        Task task = taskRepository
                .findByIdAndTasklist_Id(idTask, idList)
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado"));

        list.removeTask(task);
    }
}
