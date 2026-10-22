package com.todolist.todoserver.service;

import com.todolist.todoserver.dto.DashboardSummaryResponseDTO;
import com.todolist.todoserver.entity.Task;
import com.todolist.todoserver.entity.User;
import com.todolist.todoserver.enums.TaskPriority;
import com.todolist.todoserver.repository.TaskRepository;
import com.todolist.todoserver.repository.TasklistRepository;
import jakarta.validation.constraints.NegativeOrZero;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class DashboardService {
    private final TaskRepository taskRepository;
    private final TasklistRepository tasklistRepository;

    public DashboardService(TaskRepository taskRepository, TasklistRepository tasklistRepository) {
        this.taskRepository = taskRepository;
        this.tasklistRepository = tasklistRepository;
    }

    @Transactional(readOnly = true)
    public DashboardSummaryResponseDTO getDashboardSummary(User user) {
        LocalDate today = LocalDate.now();
        final Long userId = user.getId();

        long totalTasklists = tasklistRepository.countByUserId(userId);
        long totalTasks = taskRepository.countByTasklist_UserId(userId);
        long pendingTasks = taskRepository.countByTasklist_UserIdAndDoneFalse(userId);
        long lateTasks = taskRepository.countByTasklist_UserIdAndDoneFalseAndDueDateBefore(userId, today);
        long completedTasks = taskRepository.countByTasklist_UserIdAndDoneTrue(userId);
        long highPriorityTasks = taskRepository.countByTasklist_UserIdAndPriority(userId, TaskPriority.ALTO);
        double completionPercentage = totalTasks == 0 ? 0.0 : (double) completedTasks / totalTasks * 100;

        return new DashboardSummaryResponseDTO(
                totalTasklists,
                totalTasks,
                pendingTasks,
                lateTasks,
                completedTasks,
                highPriorityTasks,
                completionPercentage
        );
    }
}
