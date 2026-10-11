package com.todolist.todoserver.service;

import com.todolist.todoserver.dto.TaskResponseDTO;
import com.todolist.todoserver.entity.Task;
import com.todolist.todoserver.entity.TaskList;
import com.todolist.todoserver.enums.TaskPriority;
import com.todolist.todoserver.infra.errors.ResourceNotFoundException;
import com.todolist.todoserver.repository.TaskRepository;
import com.todolist.todoserver.repository.TaskListRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    private final TaskListRepository taskListRepository;
    private final TaskRepository taskRepository;

    public TaskService(TaskListRepository taskListRepository, TaskRepository taskRepository) {
        this.taskListRepository = taskListRepository;
        this.taskRepository = taskRepository;
    }

    @Transactional
    public void create(Long idList, String name, String description, TaskPriority priority, LocalDate dueDate) {
        TaskList list = taskListRepository
                .findById(idList)
                .orElseThrow(() -> new ResourceNotFoundException("Lista não encontrada"));

        Task task = new Task();

        task.setName(name);
        task.setDescription(description);
        task.setPriority(priority);
        task.setDueDate(dueDate);
        task.setDone(false);
        task.setTaskList(list);

        list.addTask(task);

        taskRepository.save(task);
    }

    @Transactional
    public void toggleDone(Long idList, Long idTask) {
        Task task = taskRepository.findByIdAndTaskList_Id(idTask, idList).orElseThrow(
                () -> new ResourceNotFoundException("Tarefa não encontrada")
        );

        task.setDone(!task.getDone());
    }

    @Transactional
    public void update(Long idList, Long idTask, String name, String description, TaskPriority priority, LocalDate dueDate) {
        Task task = taskRepository.findByIdAndTaskList_Id(idTask, idList).orElseThrow(
                () -> new ResourceNotFoundException("Tarefa não encontrada")
        );

        task.setName(name);
        task.setDescription(description);
        task.setPriority(priority);
        task.setDueDate(dueDate);
    }

    public List<TaskResponseDTO> searchAll(Long idList) {
        return taskRepository.findByTaskList_Id(idList)
                .stream()
                .map(task ->
                        new TaskResponseDTO(task.getId(), task.getName(), task.getDescription(), task.getDueDate(), task.getPriority(), task.getDone())
                )
                .toList();
    }

    @Transactional
    public void remove(Long idList, Long idTask) {
        TaskList list = taskListRepository
                .findById(idList)
                .orElseThrow(() -> new ResourceNotFoundException("Lista não encontrada"));

        Task task = taskRepository
                .findByIdAndTaskList_Id(idTask, idList)
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado"));

        list.removeTask(task);
    }
}
