package com.todolist.todoserver.service;

import com.todolist.todoserver.entity.User;
import com.todolist.todoserver.infra.errors.ResourceNotFoundException;
import com.todolist.todoserver.infra.errors.UserAlreadyExistsException;
import com.todolist.todoserver.repository.UsersRepository;
import com.todolist.todoserver.security.TokenService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            AuthenticationManager authenticationManager,
            TokenService tokenService,
            UsersRepository usersRepository, PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(String email, String password) {
        var authData = new UsernamePasswordAuthenticationToken(email, password);

        Authentication authentication = this.authenticationManager.authenticate(authData);

        User user = (User) authentication.getPrincipal();

        if (user == null) {
            throw new ResourceNotFoundException("Usuário não encontrado");
        }

        return tokenService.generateToken(user.getEmail(), user.getId());
    }

    @Transactional
    public void register(User user) {
        boolean usersExists = usersRepository.existsByEmail(user.getEmail());

        if (usersExists) {
            throw new UserAlreadyExistsException("Usuário já cadastrado");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        usersRepository.save(user);
    }
}
