package mx.edu.utez.back.controller;

import mx.edu.utez.back.model.Visit;
import mx.edu.utez.back.service.VisitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visits")
@CrossOrigin(origins = "*", maxAge = 3600)
public class VisitController {
    private final VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @PostMapping("/scan")
    public ResponseEntity<Visit> scan(@RequestParam String storeCode,
            @RequestParam Long repartidorId,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lng,
            @RequestParam(defaultValue = "false") boolean hadOrder,
            @RequestParam(defaultValue = "false") boolean temporary) {
        return ResponseEntity.ok(visitService.registerScan(storeCode, repartidorId, lat, lng, hadOrder, temporary));
    }

    @GetMapping("/by-repartidor/{id}")
    public ResponseEntity<List<Visit>> byRepartidor(@PathVariable Long id) {
        return ResponseEntity.ok(visitService.findByRepartidor(id));
    }
}
