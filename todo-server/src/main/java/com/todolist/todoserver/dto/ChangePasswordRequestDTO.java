package com.todolist.todoserver.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequestDTO(
        @NotBlank(message = "Senha é obrigatória")
        String password,
        @NotBlank(message = "Nova senha é obrigatória")
        String newPassword,
        @NotBlank(message = "Confirmar senha é obrigatório")
        String confirmPassword
) {
    @AssertTrue(message = "As senhas não coincidem")
    public boolean isPasswordMatch() {
        if (newPassword == null || confirmPassword == null) return false;
        return newPassword.equals(confirmPassword);
    }
}
