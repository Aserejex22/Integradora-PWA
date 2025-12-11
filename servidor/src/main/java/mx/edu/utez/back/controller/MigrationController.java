package mx.edu.utez.back.controller;

import mx.edu.utez.back.model.Order;
import mx.edu.utez.back.model.OrderItem;
import mx.edu.utez.back.repository.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/migration")
@CrossOrigin(origins = "*")
public class MigrationController {

    private final OrderRepository orderRepository;

    public MigrationController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Recalcula los totales de todos los pedidos existentes
     */
    @PostMapping("/recalculate-totals")
    public ResponseEntity<String> recalculateTotals() {
        try {
            List<Order> orders = orderRepository.findAll();
            int updated = 0;

            for (Order order : orders) {
                if (order.getItems() != null && !order.getItems().isEmpty()) {
                    double total = 0.0;
                    for (OrderItem item : order.getItems()) {
                        if (item.getPrice() != null && item.getQuantity() != null) {
                            total += item.getPrice() * item.getQuantity();
                        }
                    }
                    order.setTotalPrice(total);
                    orderRepository.save(order, order.getId());
                    updated++;
                }
            }

            return ResponseEntity.ok("Recalculados " + updated + " pedidos");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}
