package com.todolist.todoserver.service;

import com.todolist.todoserver.dto.ListProgressDTO;
import com.todolist.todoserver.dto.ListResponseDTO;
import com.todolist.todoserver.entity.Tasklist;
import com.todolist.todoserver.entity.User;
import com.todolist.todoserver.infra.errors.ResourceNotFoundException;
import com.todolist.todoserver.repository.TaskRepository;
import com.todolist.todoserver.repository.TasklistRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TasklistService {
    private final TasklistRepository tasklistRepository;
    private final TaskRepository taskRepository;

    public TasklistService(TasklistRepository taskListRepository, TaskRepository taskRepository) {
        this.tasklistRepository = taskListRepository;
        this.taskRepository = taskRepository;
    }

    @Transactional
    public void create(User user, String name, String description) {
        Tasklist list = new Tasklist();

        list.setName(name);
        list.setDescription(description);
        list.setUser(user);

        this.tasklistRepository.save(list);
    }

    @Transactional
    public void update(Long idList, String name, String description, User user) {
        Tasklist list = findListById(idList, user);

        list.setName(name);
        list.setDescription(description);
    }

    @Transactional
    public void delete(Long idList, User user) {
        Tasklist list = findListById(idList, user);

        tasklistRepository.delete(list);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ListResponseDTO get(Long idList, User user) {
        Tasklist list = tasklistRepository
                .findByIdAndUser(idList, user)
                .orElseThrow(() -> new ResourceNotFoundException("Lista não encontrada"));

        return listResponseDTO(list);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<ListResponseDTO> getAll(User user) {
        List<Tasklist> lists =  tasklistRepository.findByUser(user);
        return lists.stream()
                .map(this::listResponseDTO)
                .toList();
    }

    private Tasklist findListById(Long idList, User user) {
        return tasklistRepository
                .findByIdAndUser(idList, user)
                .orElseThrow(() -> new ResourceNotFoundException("Lista não encontrada"));
    }

    private ListResponseDTO listResponseDTO(Tasklist list) {
        Map<Long, ListProgressDTO> progressByListId =
                taskRepository
                        .findProgressByUserId(list.getUser().getId())
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        ListProgressDTO::listId, Function.identity()
                                )
                        );

        ListProgressDTO progress =  progressByListId.get(list.getId());

        long totalTasks = progress == null ? 0L : progress.totalTasks();
        long completedTasks = progress == null ? 0L : progress.completedTasks();
        double completionPercentage = completedTasks == 0 ? 0.0 : (double) completedTasks / totalTasks * 100;

        return new ListResponseDTO(
                list.getId(),
                list.getName(),
                list.getDescription(),
                totalTasks,
                completedTasks,
                completionPercentage
        );
    }
}
