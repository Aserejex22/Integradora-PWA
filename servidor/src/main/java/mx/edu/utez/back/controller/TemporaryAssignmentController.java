package mx.edu.utez.back.controller;

import mx.edu.utez.back.model.TemporaryAssignment;
import mx.edu.utez.back.service.TemporaryAssignmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/temporary-assignments")
@CrossOrigin(origins = "*")
public class TemporaryAssignmentController {
    private final TemporaryAssignmentService service;

    public TemporaryAssignmentController(TemporaryAssignmentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TemporaryAssignment> assign(@RequestParam Long storeId,
            @RequestParam Long repartidorId,
            @RequestParam String date) {
        return ResponseEntity.ok(service.assign(storeId, repartidorId, date));
    }

    @GetMapping("/repartidor/{repartidorId}/date/{date}")
    public ResponseEntity<List<TemporaryAssignment>> getByRepartidorAndDate(
            @PathVariable Long repartidorId,
            @PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        List<TemporaryAssignment> assignments = service.findForRepartidorOnDate(repartidorId, localDate);
        return ResponseEntity.ok(assignments);
    }
}
