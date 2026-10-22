package com.todolist.todoserver.service;

import com.todolist.todoserver.dto.TaskResponseDTO;
import com.todolist.todoserver.entity.Task;
import com.todolist.todoserver.entity.Tasklist;
import com.todolist.todoserver.entity.User;
import com.todolist.todoserver.infra.errors.ResourceNotFoundException;
import com.todolist.todoserver.infra.errors.UnauthorizedOperationException;
import com.todolist.todoserver.repository.TaskRepository;
import com.todolist.todoserver.repository.TasklistRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TasklistRepository tasklistRepository;
    private final TaskRepository taskRepository;

    public TaskService(TasklistRepository taskListRepository, TaskRepository taskRepository) {
        this.tasklistRepository = taskListRepository;
        this.taskRepository = taskRepository;
    }

    @Transactional
    public void create(Long idList, Task task, User user) {
        Tasklist list = tasklistRepository
                .findById(idList)
                .orElseThrow(() -> new ResourceNotFoundException("Lista não encontrada"));

        if (!user.getId().equals(list.getUser().getId())) {
            throw new UnauthorizedOperationException("Acesso negado");
        }

        list.addTask(task);
    }

    @Transactional
    public void toggleDone(Long idList, Long idTask, User user) {
        Task task = taskRepository.findByIdAndTasklist_Id(idTask, idList).orElseThrow(
                () -> new ResourceNotFoundException("Tarefa não encontrada")
        );

        if (!task.getTasklist().getUser().getId().equals(user.getId())) {
            throw new UnauthorizedOperationException("Acesso negado");
        }

        task.setDone(!task.getDone());
    }

    @Transactional
    public void update(Long idList, Long idTask, Task task, User user) {
        Task newTask = taskRepository.findByIdAndTasklist_Id(idTask, idList).orElseThrow(
                () -> new ResourceNotFoundException("Tarefa não encontrada")
        );

        validateOwnership(user, newTask.getTasklist());

        newTask.setName(task.getName());
        newTask.setDescription(task.getDescription());
        newTask.setPriority(task.getPriority());
        newTask.setDueDate(task.getDueDate());
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<TaskResponseDTO> getAll(Long idList, User user) {
        Tasklist tasklist = tasklistRepository.findByIdAndUser(idList, user).orElseThrow(
                () -> new ResourceNotFoundException("Lista não encontrada")
        );

        return taskRepository.findByTasklist_Id(tasklist.getId())
                .stream()
                .map(TaskResponseDTO::new)
                .toList();
    }

    @Transactional
    public void remove(Long idList, Long idTask, User user) {
        Tasklist list = tasklistRepository
                .findById(idList)
                .orElseThrow(() -> new ResourceNotFoundException("Lista não encontrada"));

        validateOwnership(user, list);

        Task task = taskRepository
                .findByIdAndTasklist_Id(idTask, idList)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada"));

        list.removeTask(task);
    }

    private void validateOwnership(User user, Tasklist list) {
        if (!list.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedOperationException("Acesso negado");
        }
    }
}
