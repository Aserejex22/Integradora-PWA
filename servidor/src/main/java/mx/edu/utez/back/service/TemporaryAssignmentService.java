package mx.edu.utez.back.service;

import mx.edu.utez.back.model.Store;
import mx.edu.utez.back.model.TemporaryAssignment;
import mx.edu.utez.back.repository.StoreRepository;
import mx.edu.utez.back.repository.TemporaryAssignmentRepository;
import mx.edu.utez.back.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TemporaryAssignmentService {
    private final TemporaryAssignmentRepository repo;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public TemporaryAssignmentService(TemporaryAssignmentRepository repo,
            StoreRepository storeRepository,
            UserRepository userRepository,
            NotificationService notificationService) {
        this.repo = repo;
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public TemporaryAssignment assign(Long storeId, Long repartidorId, String dateString) {
        try {
            Optional<Store> storeOpt = storeRepository.findById(storeId);
            if (!storeOpt.isPresent()) {
                throw new RuntimeException("Store not found");
            }
            Store store = storeOpt.get();

            if (!userRepository.findById(repartidorId).isPresent()) {
                throw new RuntimeException("User not found");
            }

            TemporaryAssignment ta = new TemporaryAssignment();
            ta.setStoreId(storeId);
            ta.setRepartidorId(repartidorId);
            ta.setDate(LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE));

            TemporaryAssignment savedTA = repo.save(ta);

            // 🔔 Enviar notificación push al repartidor
            notificationService.notificarAsignacionTemporal(repartidorId.toString(), store.getName(), dateString);

            return savedTA;
        } catch (Exception e) {
            throw new RuntimeException("Error assigning temporary store", e);
        }
    }

    public List<TemporaryAssignment> findForRepartidorOnDate(Long repartidorId, LocalDate date) {
        try {
            List<TemporaryAssignment> assignments = repo.findByDate(date).stream()
                    .filter(ta -> repartidorId.equals(ta.getRepartidorId()))
                    .collect(Collectors.toList());

            // Populate Store object for each assignment
            for (TemporaryAssignment ta : assignments) {
                if (ta.getStoreId() != null) {
                    try {
                        Optional<Store> storeOpt = storeRepository.findById(ta.getStoreId());
                        storeOpt.ifPresent(ta::setStore);
                    } catch (Exception e) {
                        // Store not found, leave null
                    }
                }
            }

            return assignments;
        } catch (Exception e) {
            throw new RuntimeException("Error finding assignments", e);
        }
    }
}
