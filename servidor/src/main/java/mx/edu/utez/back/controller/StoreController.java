package mx.edu.utez.back.controller;

import mx.edu.utez.back.model.Store;
import mx.edu.utez.back.service.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stores")
public class StoreController {
    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @PostMapping
    public ResponseEntity<Store> create(@RequestBody Store store) {
        return ResponseEntity.ok(storeService.create(store));
    }

    @PutMapping
    public ResponseEntity<Store> update(@RequestBody Store store) {
        return ResponseEntity.ok(storeService.update(store));
    }

    @GetMapping
    public ResponseEntity<List<Store>> list() {
        return ResponseEntity.ok(storeService.findAll());
    }

    @GetMapping("/by-code/{code}")
    public ResponseEntity<?> byCode(@PathVariable String code) {
        Optional<Store> store = storeService.findByCode(code);
        return store.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        storeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}