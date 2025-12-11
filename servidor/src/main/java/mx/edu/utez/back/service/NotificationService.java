package mx.edu.utez.back.service;

import mx.edu.utez.back.model.Notification;
import mx.edu.utez.back.model.Role;
import mx.edu.utez.back.repository.NotificationRepository;
import mx.edu.utez.back.repository.UserRepository;
import mx.edu.utez.back.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio de notificaciones - Almacenamiento en MySQL
 * 
 * @author TiendaPWA Team
 */
@Service
public class NotificationService {
    private final NotificationRepository repo;
    private final UserRepository userRepo;

    public NotificationService(NotificationRepository repo, UserRepository userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }

    /**
     * Registrar evento genérico
     */
    public void registrarEvento(String title, String message, String type) {
        try {
            guardarNotificacion(title, message, type);
            System.out.println("✅ Evento registrado: " + title);
        } catch (Exception e) {
            System.err.println("Error registrando evento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * CASO 1: Notificar cuando se crea un nuevo pedido
     */
    public void notificarNuevoPedido(String orderId, String storeName, Double totalPrice) {
        try {
            String titulo = "🛒 Nuevo Pedido Recibido";
            String mensaje = String.format("Pedido de %s por $%.2f", storeName, totalPrice);

            guardarNotificacion(titulo, mensaje, "nuevo_pedido");
            System.out.println("✅ Notificación de nuevo pedido guardada");
        } catch (Exception e) {
            System.err.println("❌ Error notificando nuevo pedido: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * CASO 2: Notificar asignación temporal
     */
    public void notificarAsignacionTemporal(String repartidorId, String storeName, String fecha) {
        try {
            try {
                Long repId = Long.parseLong(repartidorId);
                Optional<User> repartidor = userRepo.findById(repId);
                if (!repartidor.isPresent()) {
                    System.out.println("⚠️ Repartidor no encontrado: " + repartidorId);
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️ ID de repartidor inválido: " + repartidorId);
                return;
            }

            String titulo = "📅 Nueva Visita Temporal Asignada";
            String mensaje = String.format("Se te asignó visitar \"%s\" el %s", storeName, fecha);

            guardarNotificacion(titulo, mensaje, "asignacion_temporal");
            System.out.println("✅ Notificación de asignación temporal guardada");
        } catch (Exception e) {
            System.err.println("❌ Error notificando asignación temporal: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * CASO 3: Notificar cambio de estado del pedido
     */
    public void notificarCambioEstadoPedido(String orderId, String nuevoEstado, String storeName) {
        try {
            String titulo = "📦 Actualización de Pedido";
            String mensaje = String.format("El pedido de %s cambió a: %s", storeName, nuevoEstado);

            guardarNotificacion(titulo, mensaje, "cambio_estado");
            System.out.println("✅ Notificación de cambio de estado guardada");
        } catch (Exception e) {
            System.err.println("❌ Error notificando cambio de estado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * CASO 4: Notificar a un repartidor específico
     */
    public void notificarRepartidor(String repartidorId, String titulo, String mensaje) {
        try {
            try {
                Long repId = Long.parseLong(repartidorId);
                Optional<User> repartidor = userRepo.findById(repId);
                if (!repartidor.isPresent()) {
                    System.out.println("⚠️ Repartidor no encontrado: " + repartidorId);
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️ ID de repartidor inválido: " + repartidorId);
                return;
            }

            guardarNotificacion(titulo, mensaje, "notificacion_repartidor");
            System.out.println("✅ Notificación enviada al repartidor");
        } catch (Exception e) {
            System.err.println("❌ Error notificando repartidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Guardar notificación en la base de datos MySQL
     */
    private void guardarNotificacion(String title, String message, String type) {
        try {
            Notification n = new Notification();
            n.setTitle(title);
            n.setMessage(message);
            n.setType(type);
            n.setDate(LocalDateTime.now());
            repo.save(n);
            System.out.println("💾 Notificación guardada en BD");
        } catch (Exception e) {
            System.err.println("❌ Error guardando notificación: " + e.getMessage());
        }
    }

    /**
     * Obtener historial de notificaciones
     */
    public List<Notification> findAll() {
        try {
            return repo.findAll();
        } catch (Exception e) {
            System.err.println("❌ Error obteniendo notificaciones: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    /**
     * Obtener notificaciones por tipo
     */
    public List<Notification> findByType(String type) {
        try {
            return repo.findByType(type);
        } catch (Exception e) {
            System.err.println("❌ Error obteniendo notificaciones: " + e.getMessage());
            return List.of();
        }
    }
}
