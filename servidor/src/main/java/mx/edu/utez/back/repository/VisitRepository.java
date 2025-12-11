package mx.edu.utez.back.repository;

import mx.edu.utez.back.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findByRepartidorId(Long repartidorId);
    List<Visit> findByStoreId(Long storeId);
}
