package mx.edu.utez.back.repository;

import mx.edu.utez.back.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStoreId(Long storeId);
    List<Order> findByRepartidorId(Long repartidorId);
    List<Order> findByStatus(String status);
}
