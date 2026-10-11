package com.todolist.todoserver.service;

import com.todolist.todoserver.entity.User;
import com.todolist.todoserver.infra.errors.PasswordMissmatchException;
import com.todolist.todoserver.infra.errors.ResourceNotFoundException;
import com.todolist.todoserver.repository.UsersRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void changePassword(Long idUser, String newPassword) {
        User user = usersRepository.findById(idUser)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        user.setPassword(passwordEncoder.encode(newPassword));

        usersRepository.save(user);
    }
}
