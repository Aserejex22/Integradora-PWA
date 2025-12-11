package mx.edu.utez.back.repository;

import mx.edu.utez.back.model.TemporaryAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TemporaryAssignmentRepository extends JpaRepository<TemporaryAssignment, Long> {
    List<TemporaryAssignment> findByRepartidorId(Long repartidorId);
    List<TemporaryAssignment> findByStoreId(Long storeId);
    List<TemporaryAssignment> findByDate(LocalDate date);
}
