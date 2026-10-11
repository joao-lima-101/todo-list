package com.todolist.todoserver.repository;

import com.todolist.todoserver.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByIdAndTaskList_Id(Long id, Long listId);
    List<Task> findByTaskList_Id(Long listId);
}
