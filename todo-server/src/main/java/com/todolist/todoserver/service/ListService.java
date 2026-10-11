package com.todolist.todoserver.service;

import com.todolist.todoserver.dto.ListResponseDTO;
import com.todolist.todoserver.entity.TaskList;
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

        TaskList list = new TaskList();

        list.setName(name);
        list.setDescription(description);
        list.setUser(user);

        this.taskListRepository.save(list);
    }

    @Transactional
    public void update(Long idList, String name, String description) {
        TaskList list = taskListRepository.findById(idList).orElseThrow(
                () -> new ResourceNotFoundException("Lista não encontrada")
        );

        list.setName(name);
        list.setDescription(description);
    }

    public void delete(Long idList) {
        TaskList list = taskListRepository
                .findById(idList)
                .orElseThrow(() -> new ResourceNotFoundException("Lista não encontrada"));

        taskListRepository.delete(list);
    }

    public ListResponseDTO getList(Long idList) {
        TaskList list = taskListRepository.findById(idList).orElseThrow(() -> new ResourceNotFoundException("List"));

        return new ListResponseDTO(list.getId(), list.getName(), list.getDescription());
    }

    public List<ListResponseDTO> getLists(Long idUser) {
        User user = usersRepository.findById(idUser)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        return taskListRepository.findByUser(user)
                .stream()
                .map(ls -> new ListResponseDTO(ls.getId(), ls.getName(), ls.getDescription()))
                .toList();
    }
}
