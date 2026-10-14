package com.todolist.todoserver.controller;

import com.todolist.todoserver.dto.ApiResponseDTO;
import com.todolist.todoserver.dto.LoginRequestDTO;
import com.todolist.todoserver.dto.RegisterRequestDTO;
import com.todolist.todoserver.dto.UserResponseDTO;
import com.todolist.todoserver.entity.User;
import com.todolist.todoserver.mapper.UserMapper;
import com.todolist.todoserver.security.TokenService;
import com.todolist.todoserver.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final TokenService tokenService;
    private final UserMapper userMapper;

    public AuthController(AuthService authService, TokenService tokenService, UserMapper userMapper) {
        this.authService = authService;
        this.tokenService = tokenService;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO> register(@Valid @RequestBody RegisterRequestDTO dto) {
        authService.register(userMapper.toEntity(dto));

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO("Cadastro realizado com sucesso"));
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Boolean>> status(@CookieValue(name = "token", required = false) String token) {
        boolean isAuthenticated = (token != null) && (tokenService.validateToken(token) != null);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("authenticated", isAuthenticated));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> me(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(new UserResponseDTO(user.getId(), user.getName(), user.getEmail()));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO dto,
            HttpServletResponse response
    ) {
        String token = authService.login(dto.email(), dto.password());

        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(900)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(new ApiResponseDTO("Login realizado com sucesso"));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDTO> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);

        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        return ResponseEntity.ok(new ApiResponseDTO("Logout realizado com sucesso"));
    }
}
