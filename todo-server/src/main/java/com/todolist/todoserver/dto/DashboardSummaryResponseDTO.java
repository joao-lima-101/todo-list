package com.todolist.todoserver.dto;

public record DashboardSummaryResponseDTO(
        long totalTasklists,
        long totalTasks,
        long pendingTasks,
        long lateTasks,
        long completedTasks,
        long highPriorityTasks,
        double completionPercentage
) {}
