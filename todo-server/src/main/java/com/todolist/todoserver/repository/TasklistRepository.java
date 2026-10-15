package com.todolist.todoserver.repository;

import com.todolist.todoserver.entity.Tasklist;
import com.todolist.todoserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TasklistRepository extends JpaRepository<Tasklist, Long> {
    List<Tasklist> findByUser(User user);
    Optional<Tasklist> findByIdAndUser(Long id, User user);
}
