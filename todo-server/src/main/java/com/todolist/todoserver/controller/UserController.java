package com.todolist.todoserver.controller;

import com.todolist.todoserver.dto.ApiResponseDTO;
import com.todolist.todoserver.dto.ChangePasswordRequestDTO;
import com.todolist.todoserver.entity.User;
import com.todolist.todoserver.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponseDTO> changePassword(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ChangePasswordRequestDTO dto
    ) {
        userService.changePassword(user.getId(), dto.newPassword());
        return ResponseEntity.ok(new ApiResponseDTO("Senha alterada com sucesso"));
    }
}
