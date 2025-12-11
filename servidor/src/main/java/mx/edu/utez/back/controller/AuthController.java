package mx.edu.utez.back.controller;

import mx.edu.utez.back.dto.AuthRequest;
import mx.edu.utez.back.dto.AuthResponse;
import mx.edu.utez.back.model.Role;
import mx.edu.utez.back.model.User;
import mx.edu.utez.back.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            Optional<User> userOpt = userService.authenticate(request.getEmail(), request.getPassword());
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                AuthResponse response = new AuthResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole().toString(),
                        "Login exitoso");
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.status(401)
                    .body(new AuthResponse(null, null, null, null, "Email o contraseña incorrectos"));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(new AuthResponse(null, null, null, null, e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        try {
            User user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            user.setRole(Role.REPARTIDOR);

            User saved = userService.create(user);

            AuthResponse response = new AuthResponse(
                    saved.getId(),
                    saved.getName(),
                    saved.getEmail(),
                    saved.getRole().toString(),
                    "Registro exitoso");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new AuthResponse(null, null, null, null, "Error: " + e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(new AuthResponse(null, null, null, null, "Logout exitoso"));
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}
