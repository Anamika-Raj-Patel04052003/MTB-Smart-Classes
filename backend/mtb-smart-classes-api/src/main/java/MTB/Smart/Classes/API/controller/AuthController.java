package MTB.Smart.Classes.API.controller;

import MTB.Smart.Classes.API.dto.AuthResponse;
import MTB.Smart.Classes.API.dto.LoginRequest;
import MTB.Smart.Classes.API.dto.RegisterRequest;
import MTB.Smart.Classes.API.entity.User;
import MTB.Smart.Classes.API.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerStudent(
            @Valid @RequestBody RegisterRequest request) {
        User user = authService.registerStudent(request);

        AuthResponse response = new AuthResponse(
                null,
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().name(),
                "Student registration successful");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }
}