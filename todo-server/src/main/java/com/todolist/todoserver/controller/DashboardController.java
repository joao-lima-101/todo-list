package com.todolist.todoserver.controller;

import com.todolist.todoserver.dto.DashboardSummaryResponseDTO;
import com.todolist.todoserver.entity.User;
import com.todolist.todoserver.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryResponseDTO> getDashboardSummary(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(dashboardService.getDashboardSummary(user));
    }
}
