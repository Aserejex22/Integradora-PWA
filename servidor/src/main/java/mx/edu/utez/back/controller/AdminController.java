package mx.edu.utez.back.controller;

import mx.edu.utez.back.model.Role;
import mx.edu.utez.back.model.User;
import mx.edu.utez.back.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/setup")
public class AdminController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/init")
    public ResponseEntity<String> initializeData() {
        try {
            System.out.println("--- MANUAL TRIGGER: INICIANDO CARGA DE USUARIOS ---");

            // 1. Verificar si ya existen para no duplicar (basado en email es mejor, pero
            // por ahora count/empty)
            List<User> existingUsers = userRepo.findAll();
            if (!existingUsers.isEmpty()) {
                return ResponseEntity.badRequest().body(
                        "Error: Ya existen usuarios en la base de datos. Borra la colección 'users' en Firebase si quieres reiniciar.");
            }

            // 1. ADMIN
            User admin = new User();
            admin.setName("Admin Principal");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            userRepo.save(admin, null);

            // 2. REPARTIDOR 1
            User repartidor = new User();
            repartidor.setName("Repartidor Centro");
            repartidor.setEmail("rep1@example.com");
            repartidor.setPassword(passwordEncoder.encode("rep123"));
            repartidor.setRole(Role.REPARTIDOR);
            userRepo.save(repartidor, null);

            // 3. REPARTIDOR 2
            User repartidor2 = new User();
            repartidor2.setName("Repartidor Polanco");
            repartidor2.setEmail("rep2@example.com");
            repartidor2.setPassword(passwordEncoder.encode("rep123"));
            repartidor2.setRole(Role.REPARTIDOR);
            userRepo.save(repartidor2, null);

            return ResponseEntity.ok("Usuarios creados exitosamente! Credenciales: admin@example.com / admin123");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error crítico al crear usuarios: " + e.getMessage());
        }
    }
}
