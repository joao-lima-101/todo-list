package com.todolist.todoserver.repository;

import com.todolist.todoserver.entity.TaskList;
import com.todolist.todoserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskListRepository extends JpaRepository<TaskList, Long> {
    List<TaskList> findByUser(User user);
}
