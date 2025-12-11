package mx.edu.utez.back.service;

import mx.edu.utez.back.model.*;
import mx.edu.utez.back.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepo;
    private final ProductRepository productRepo;
    private final StoreRepository storeRepo;
    private final UserRepository userRepo;
    private final NotificationService notificationService;

    public OrderService(OrderRepository orderRepo,
            ProductRepository productRepo,
            StoreRepository storeRepo,
            UserRepository userRepo,
            NotificationService notificationService) {
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
        this.storeRepo = storeRepo;
        this.userRepo = userRepo;
        this.notificationService = notificationService;
    }

    public List<Order> findAll() {
        try {
            List<Order> orders = orderRepo.findAll();

            // Populate related objects for each order
            for (Order order : orders) {
                // Populate Store
                if (order.getStoreId() != null) {
                    try {
                        Optional<Store> store = storeRepo.findById(order.getStoreId());
                        store.ifPresent(order::setStore);
                    } catch (Exception e) {
                        // Store not found, leave null
                    }
                }

                // Populate Repartidor
                if (order.getRepartidorId() != null) {
                    try {
                        Optional<User> repartidor = userRepo.findById(order.getRepartidorId());
                        repartidor.ifPresent(order::setRepartidor);
                    } catch (Exception e) {
                        // User not found, leave null
                    }
                }

                // Populate Products in OrderItems
                if (order.getItems() != null) {
                    for (OrderItem item : order.getItems()) {
                        if (item.getProductId() != null) {
                            try {
                                Optional<Product> product = productRepo.findById(item.getProductId());
                                product.ifPresent(item::setProduct);
                            } catch (Exception e) {
                                // Product not found, leave null
                            }
                        }
                    }
                }
            }

            return orders;
        } catch (Exception e) {
            throw new RuntimeException("Error finding orders", e);
        }
    }

    public Order create(Order orderData) {
        try {
            Order newOrder = new Order();
            newOrder.setClientName(orderData.getClientName());
            newOrder.setStatus("PENDIENTE");
            newOrder.setCreatedAt(LocalDateTime.now());

            // 1. Asignar Tienda (Validar que exista)
            if (orderData.getStoreId() != null) {
                Optional<Store> store = storeRepo.findById(orderData.getStoreId());
                if (!store.isPresent()) {
                    throw new RuntimeException("Tienda no encontrada");
                }
                newOrder.setStoreId(store.get().getId());
            }

            // 2. Procesar Productos
            double totalCalculado = 0.0;
            List<OrderItem> itemsFinales = new ArrayList<>();

            if (orderData.getItems() != null) {
                for (OrderItem itemDTO : orderData.getItems()) {
                    // Validar producto
                    if (itemDTO.getProductId() == null)
                        continue;

                    Optional<Product> productOpt = productRepo.findById(itemDTO.getProductId());
                    if (!productOpt.isPresent()) {
                        throw new RuntimeException("Producto no encontrado: " + itemDTO.getProductId());
                    }

                    Product p = productOpt.get();
                    if (p.getStock() < itemDTO.getQuantity()) {
                        throw new RuntimeException("Stock insuficiente para: " + p.getName());
                    }

                    // Actualizar stock
                    p.setStock(p.getStock() - itemDTO.getQuantity());
                    productRepo.save(p);

                    // Crear Item
                    OrderItem item = new OrderItem();
                    item.setProductId(p.getId());
                    item.setQuantity(itemDTO.getQuantity());
                    item.setPrice(p.getPrice());

                    itemsFinales.add(item);
                    totalCalculado += p.getPrice() * itemDTO.getQuantity();
                }
            }

            newOrder.setItems(itemsFinales);
            newOrder.setTotalPrice(totalCalculado);

            // Clear nested objects before saving
            newOrder.setStore(null);
            newOrder.setRepartidor(null);
            for (OrderItem item : newOrder.getItems()) {
                item.setProduct(null);
            }

            // Guardar orden
            Order savedOrder = orderRepo.save(newOrder);

            // 🔔 3. Notificar creación del pedido
            notificationService.notificarNuevoPedido(
                    savedOrder.getId().toString(),
                    savedOrder.getClientName(),
                    savedOrder.getTotalPrice());

            return savedOrder;
        } catch (Exception e) {
            throw new RuntimeException("Error creating order", e);
        }
    }

    public Order update(Long id, Order orderDetails) {
        try {
            Optional<Order> orderOpt = orderRepo.findById(id);
            if (!orderOpt.isPresent()) {
                throw new RuntimeException("Pedido no encontrado");
            }

            Order order = orderOpt.get();

            // Actualizar estado
            if (orderDetails.getStatus() != null) {
                String oldStatus = order.getStatus();
                order.setStatus(orderDetails.getStatus());

                // 🔔 4. Notificar si se completó la entrega
                if (!"COMPLETADO".equals(oldStatus) && "COMPLETADO".equals(orderDetails.getStatus())) {
                    notificationService.registrarEvento(
                            "Entrega Exitosa",
                            "El pedido #" + order.getId() + " ha sido entregado al cliente.",
                            "success");
                }
            }

            // Asignar repartidor
            if (orderDetails.getRepartidorId() != null) {
                Optional<User> rep = userRepo.findById(orderDetails.getRepartidorId());
                if (rep.isPresent()) {
                    order.setRepartidorId(rep.get().getId());
                }
            }

            return orderRepo.save(order);
        } catch (Exception e) {
            throw new RuntimeException("Error updating order", e);
        }
    }

    public void delete(Long id) {
        try {
            orderRepo.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting order", e);
        }
    }
}