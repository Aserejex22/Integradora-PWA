package mx.edu.utez.back.service;

import mx.edu.utez.back.model.Store;
import mx.edu.utez.back.model.User;
import mx.edu.utez.back.model.Visit;
import mx.edu.utez.back.repository.StoreRepository;
import mx.edu.utez.back.repository.UserRepository;
import mx.edu.utez.back.repository.VisitRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VisitService {
    private final VisitRepository visitRepo;
    private final StoreRepository storeRepo;
    private final UserRepository userRepo;
    private final NotificationService notificationService;

    public VisitService(VisitRepository visitRepo, StoreRepository storeRepo, UserRepository userRepo,
            NotificationService notificationService) {
        this.visitRepo = visitRepo;
        this.storeRepo = storeRepo;
        this.userRepo = userRepo;
        this.notificationService = notificationService;
    }

    public List<Visit> findAll() {
        try {
            return visitRepo.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error finding visits", e);
        }
    }

    public List<Visit> findByRepartidor(Long repartidorId) {
        try {
            return visitRepo.findByRepartidorId(repartidorId);
        } catch (Exception e) {
            throw new RuntimeException("Error finding visits for repartidor", e);
        }
    }

    public Visit registerScan(String storeCode, Long repartidorId, Double lat, Double lng, boolean hadOrder,
            boolean temporary) {
        try {
            // 1. Validar Tienda por código
            Optional<Store> storeOpt = storeRepo.findByCode(storeCode);
            if (!storeOpt.isPresent()) {
                throw new RuntimeException("Código de tienda inválido");
            }
            Store store = storeOpt.get();

            // 2. Validar Repartidor
            Optional<User> repartidorOpt = userRepo.findById(repartidorId);
            if (!repartidorOpt.isPresent()) {
                throw new RuntimeException("Repartidor no encontrado");
            }
            User repartidor = repartidorOpt.get();

            // 3. Crear Visita
            Visit visit = new Visit();
            visit.setStoreId(store.getId());
            visit.setRepartidorId(repartidor.getId());
            visit.setCheckInTime(LocalDateTime.now());
            visit.setLatitude(lat);
            visit.setLongitude(lng);
            visit.setHadOrder(hadOrder);
            visit.setTemporaryAssignment(temporary);

            Visit savedVisit = visitRepo.save(visit);

            // 🔔 4. ENVIAR NOTIFICACIÓN AL ADMINISTRADOR
            notificationService.registrarEvento(
                    "Visita Registrada",
                    "El repartidor " + repartidor.getName() + " ha llegado a " + store.getName(),
                    "info");

            return savedVisit;
        } catch (Exception e) {
            throw new RuntimeException("Error registering scan", e);
        }
    }
}