package com.todolist.todoserver.service;

import com.todolist.todoserver.dto.ListResponseDTO;
import com.todolist.todoserver.entity.Tasklist;
import com.todolist.todoserver.entity.User;
import com.todolist.todoserver.infra.errors.ResourceNotFoundException;
import com.todolist.todoserver.repository.TaskListRepository;
import com.todolist.todoserver.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListService {
    private final TaskListRepository taskListRepository;
    private final UsersRepository usersRepository;

    public ListService(TaskListRepository taskListRepository, UsersRepository usersRepository) {
        this.taskListRepository = taskListRepository;
        this.usersRepository = usersRepository;
    }

    @Transactional
    public void create(Long idUser, String name, String description) {
        User user = usersRepository.findById(idUser)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Tasklist list = new Tasklist();

        list.setName(name);
        list.setDescription(description);
        list.setUser(user);

        this.taskListRepository.save(list);
    }

    @Transactional
    public void update(Long idList, String name, String description) {
        Tasklist list = taskListRepository.findById(idList).orElseThrow(
                () -> new ResourceNotFoundException("Lista não encontrada")
        );

        list.setName(name);
        list.setDescription(description);
    }

    public void delete(Long idList) {
        Tasklist list = taskListRepository
                .findById(idList)
                .orElseThrow(() -> new ResourceNotFoundException("Lista não encontrada"));

        taskListRepository.delete(list);
    }

    public ListResponseDTO getList(Long idList) {
        Tasklist list = taskListRepository.findById(idList).orElseThrow(() -> new ResourceNotFoundException("List"));

        return new ListResponseDTO(list);
    }

    public List<ListResponseDTO> getLists(Long idUser) {
        User user = usersRepository.findById(idUser)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        return taskListRepository.findByUser(user)
                .stream()
                .map(ListResponseDTO::new)
                .toList();
    }
}
