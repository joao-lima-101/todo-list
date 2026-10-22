package com.todolist.todoserver.repository;

import com.todolist.todoserver.dto.ListProgressDTO;
import com.todolist.todoserver.entity.Task;
import com.todolist.todoserver.enums.TaskPriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByIdAndTasklist_Id(Long id, Long listId);
    List<Task> findByTasklist_Id(Long listId);
    long countByTasklist_UserId(Long userId);
    long countByTasklist_UserIdAndDoneFalse(Long userId);
    long countByTasklist_UserIdAndDoneTrue(Long userId);
    long countByTasklist_UserIdAndDoneFalseAndDueDateBefore(
            Long userId,
            LocalDate today
    );
    long countByTasklist_UserIdAndPriority(
            Long userId,
            TaskPriority priority
    );
    @Query("""
    SELECT new com.todolist.todoserver.dto.ListProgressDTO(
        t.tasklist.id,
        COUNT(t),
        SUM(CASE WHEN t.done = true THEN 1 ELSE 0 END)
    )
    FROM Task t
    WHERE t.tasklist.user.id = :userId
    GROUP BY t.tasklist.id
    """)
    List<ListProgressDTO> findProgressByUserId(@Param("userId") Long userId );
}
