package mx.edu.utez.back.controller;

import mx.edu.utez.back.model.Notification;
import mx.edu.utez.back.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {
    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Notification>> listar() {
        return ResponseEntity.ok(service.findAll());
    }
}