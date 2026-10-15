package com.todolist.todoserver.service;

import com.todolist.todoserver.dto.ListResponseDTO;
import com.todolist.todoserver.entity.Tasklist;
import com.todolist.todoserver.entity.User;
import com.todolist.todoserver.infra.errors.ResourceNotFoundException;
import com.todolist.todoserver.repository.TasklistRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TasklistService {
    private final TasklistRepository tasklistRepository;

    public TasklistService(TasklistRepository taskListRepository) {
        this.tasklistRepository = taskListRepository;
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

        return new ListResponseDTO(list);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<ListResponseDTO> getAll(User user) {
        return tasklistRepository.findByUser(user)
                .stream()
                .map(ListResponseDTO::new)
                .toList();
    }

    private Tasklist findListById(Long idList, User user) {
        return tasklistRepository
                .findByIdAndUser(idList, user)
                .orElseThrow(() -> new ResourceNotFoundException("Lista não encontrada"));
    }
}
