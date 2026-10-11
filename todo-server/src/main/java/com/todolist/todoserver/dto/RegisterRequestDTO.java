package com.todolist.todoserver.dto;

import jakarta.validation.constraints.*;

public record RegisterRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        String name,
        String lastname,
        @Email(message = "E-mail inválido")
        @NotBlank(message = "E-mail é obrigatório")
        String email,
        @Size(min = 8, message = "Senha deve possuir no minimo 8 caracteres")
        @NotBlank(message = "Senha é obrigatória")
        String password,
        @NotBlank(message = "Confirmação de senha é obrigatória")
        String confirmPassword
) {
    @AssertTrue(message = "As senhas não coincidem")
    public boolean isPasswordMatch() {
        if (password == null || confirmPassword == null) return false;
        return password.equals(confirmPassword);
    }
}
