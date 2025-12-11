package mx.edu.utez.back.config;

import mx.edu.utez.back.model.*;
import mx.edu.utez.back.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(
            UserRepository userRepo,
            StoreRepository storeRepo,
            ProductRepository productRepo,
            PasswordEncoder passwordEncoder) {
        return args -> {
            try {
                List<User> existingUsers = userRepo.findAll();
                if (existingUsers.isEmpty()) {
                    System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                    System.out.println("🚀 INICIANDO CARGA DE DATOS DE PRUEBA (FIRESTORE)");
                    System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

                    // ═══════════════════════════════════════════════════
                    // 🏢 PASO 1: CREAR TIENDAS
                    // ═══════════════════════════════════════════════════
                    System.out.println("\n📍 Creando tiendas de prueba...");

                    Store store1 = new Store();
                    store1.setName("Abarrotes Don Pepe");
                    store1.setCode("QR-001");
                    store1.setCity("CDMX");
                    store1.setAddress("Av. Insurgentes Sur 1234, Col. Del Valle");
                    store1.setPhone("5551234567");
                    store1.setLatitude(19.432608);
                    store1.setLongitude(-99.133209);
                    String store1Id = storeRepo.save(store1, null);
                    store1.setId(store1Id);
                    System.out.println("   ✅ " + store1.getName() + " (ID: " + store1Id + ")");

                    Store store2 = new Store();
                    store2.setName("Tienda La Providencia");
                    store2.setCode("QR-002");
                    store2.setCity("CDMX");
                    store2.setAddress("Calle Polanco 567, Col. Polanco");
                    store2.setPhone("5559876543");
                    store2.setLatitude(19.433333);
                    store2.setLongitude(-99.191111);
                    String store2Id = storeRepo.save(store2, null);
                    store2.setId(store2Id);
                    System.out.println("   ✅ " + store2.getName() + " (ID: " + store2Id + ")");

                    Store store3 = new Store();
                    store3.setName("Super Mercado Central");
                    store3.setCode("QR-003");
                    store3.setCity("CDMX");
                    store3.setAddress("Av. Juárez 890, Col. Centro");
                    store3.setPhone("5552223344");
                    store3.setLatitude(19.434444);
                    store3.setLongitude(-99.144444);
                    String store3Id = storeRepo.save(store3, null);
                    store3.setId(store3Id);
                    System.out.println("   ✅ " + store3.getName() + " (ID: " + store3Id + ")");

                    // ═══════════════════════════════════════════════════
                    // 👤 PASO 2: CREAR USUARIOS
                    // ═══════════════════════════════════════════════════
                    System.out.println("\n👥 Creando usuarios de prueba...");

                    // 1. ADMIN
                    User admin = new User();
                    admin.setName("Admin Principal");
                    admin.setEmail("admin@example.com");
                    admin.setPassword(passwordEncoder.encode("admin123"));
                    admin.setRole(Role.ADMIN);
                    String adminId = userRepo.save(admin, null);
                    admin.setId(adminId);
                    System.out.println("   ✅ ADMIN: " + admin.getEmail() + " / admin123");

                    // 2. REPARTIDOR 1 - Asignado a Tienda 1
                    User repartidor1 = new User();
                    repartidor1.setName("Juan Pérez - Repartidor Centro");
                    repartidor1.setEmail("rep1@example.com");
                    repartidor1.setPassword(passwordEncoder.encode("rep123"));
                    repartidor1.setRole(Role.REPARTIDOR);
                    repartidor1.setMainStoreId(store1Id); // ✨ ASIGNACIÓN DE TIENDA
                    String rep1Id = userRepo.save(repartidor1, null);
                    repartidor1.setId(rep1Id);
                    System.out.println("   ✅ REPARTIDOR 1: " + repartidor1.getEmail() + " / rep123");
                    System.out.println("      → Asignado a: " + store1.getName());

                    // 3. REPARTIDOR 2 - Asignado a Tienda 2
                    User repartidor2 = new User();
                    repartidor2.setName("María López - Repartidor Polanco");
                    repartidor2.setEmail("rep2@example.com");
                    repartidor2.setPassword(passwordEncoder.encode("rep123"));
                    repartidor2.setRole(Role.REPARTIDOR);
                    repartidor2.setMainStoreId(store2Id); // ✨ ASIGNACIÓN DE TIENDA
                    String rep2Id = userRepo.save(repartidor2, null);
                    repartidor2.setId(rep2Id);
                    System.out.println("   ✅ REPARTIDOR 2: " + repartidor2.getEmail() + " / rep123");
                    System.out.println("      → Asignado a: " + store2.getName());

                    // 4. REPARTIDOR 3 - Sin tienda asignada (para pruebas)
                    User repartidor3 = new User();
                    repartidor3.setName("Carlos Ramírez - Repartidor Sin Asignar");
                    repartidor3.setEmail("rep3@example.com");
                    repartidor3.setPassword(passwordEncoder.encode("rep123"));
                    repartidor3.setRole(Role.REPARTIDOR);
                    // mainStoreId = null (sin asignación)
                    String rep3Id = userRepo.save(repartidor3, null);
                    repartidor3.setId(rep3Id);
                    System.out.println("   ✅ REPARTIDOR 3: " + repartidor3.getEmail() + " / rep123");
                    System.out.println("      → Sin tienda asignada (para pruebas)");

                    // ═══════════════════════════════════════════════════
                    // 📦 PASO 3: CREAR PRODUCTOS DE PRUEBA
                    // ═══════════════════════════════════════════════════
                    System.out.println("\n📦 Creando productos de prueba...");

                    String[] productNames = {
                            "Coca-Cola 600ml", "Sabritas Original", "Pan Bimbo Blanco",
                            "Leche Lala 1L", "Huevos San Juan 12pz", "Aceite 123 1L",
                            "Azúcar Estándar 1kg", "Frijol Negro 1kg", "Arroz Morelos 1kg",
                            "Detergente Ariel 1kg"
                    };

                    String[] skus = {
                            "BEB-001", "PS-01", "PAN-001",
                            "LAC-001", "HUE-001", "ACE-001",
                            "AZU-001", "FRI-001", "ARR-001",
                            "LIM-001"
                    };

                    String[] descriptions = {
                            "Refresco sabor cola de 600ml, ideal para acompañar tus comidas.",
                            "Papas fritas clásicas, crujientes y con el toque exacto de sal.",
                            "Pan blanco grande, suave y perfecto para sándwiches.",
                            "Leche entera pasteurizada, rica en calcio y vitaminas.",
                            "Huevo blanco fresco selecccionado, paquete con 12 piezas.",
                            "Aceite vegetal comestible, ideal para cocinar todos tus platillos.",
                            "Azúcar de caña estándar, endulzante natural.",
                            "Frijol negro limpio, seleccionado y listo para cocer.",
                            "Arroz súper extra grano largo, no se bate ni se pega.",
                            "Detergente en polvo multiusos, arranca la grasa y las manchas."
                    };

                    double[] prices = { 15.0, 18.0, 35.0, 22.0, 45.0, 38.0, 28.0, 32.0, 25.0, 65.0 };
                    int[] stocks = { 50, 40, 30, 25, 20, 15, 35, 28, 30, 18 };

                    for (int i = 0; i < productNames.length; i++) {
                        Product product = new Product();
                        product.setName(productNames[i]);
                        product.setSku(skus[i]);
                        product.setDescription(descriptions[i]);
                        product.setPrice(prices[i]);
                        product.setStock(stocks[i]);
                        productRepo.save(product, null);
                        System.out.println("   ✅ " + productNames[i] + " ($" + prices[i] + ") [" + skus[i] + "]");
                    }

                    // ═══════════════════════════════════════════════════
                    // ✅ RESUMEN FINAL
                    // ═══════════════════════════════════════════════════
                    System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                    System.out.println("✅ DATOS DE PRUEBA CREADOS EXITOSAMENTE");
                    System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                    System.out.println("\n📋 CREDENCIALES DE ACCESO:");
                    System.out.println("┌─────────────────────────────────────────────────┐");
                    System.out.println("│ 🔐 ADMIN                                        │");
                    System.out.println("│    Email: admin@example.com                     │");
                    System.out.println("│    Pass:  admin123                              │");
                    System.out.println("├─────────────────────────────────────────────────┤");
                    System.out.println("│ 🚚 REPARTIDOR 1 (Con tienda asignada)          │");
                    System.out.println("│    Email: rep1@example.com                      │");
                    System.out.println("│    Pass:  rep123                                │");
                    System.out.println("│    Tienda: Abarrotes Don Pepe                   │");
                    System.out.println("├─────────────────────────────────────────────────┤");
                    System.out.println("│ 🚚 REPARTIDOR 2 (Con tienda asignada)          │");
                    System.out.println("│    Email: rep2@example.com                      │");
                    System.out.println("│    Pass:  rep123                                │");
                    System.out.println("│    Tienda: Tienda La Providencia               │");
                    System.out.println("├─────────────────────────────────────────────────┤");
                    System.out.println("│ 🚚 REPARTIDOR 3 (SIN tienda asignada)          │");
                    System.out.println("│    Email: rep3@example.com                      │");
                    System.out.println("│    Pass:  rep123                                │");
                    System.out.println("│    Tienda: Ninguna (para pruebas)              │");
                    System.out.println("└─────────────────────────────────────────────────┘");
                    System.out.println("\n🏢 TIENDAS CREADAS: 3");
                    System.out.println("📦 PRODUCTOS CREADOS: " + productNames.length);
                    System.out.println("\n🎉 ¡Listo para comenzar a probar la PWA!\n");

                } else {
                    System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                    System.out.println("ℹ️  Datos ya existen en BD, omitiendo carga inicial");
                    System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                }
            } catch (Exception e) {
                System.err.println("❌ Error initializing data: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}
