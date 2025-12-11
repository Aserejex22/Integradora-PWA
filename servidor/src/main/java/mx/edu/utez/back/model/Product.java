package mx.edu.utez.back.model;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer stock;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @Column(nullable = false)
    private Boolean available;

    @Column(name = "photo_base64", columnDefinition = "LONGBLOB")
    private String photoBase64;

    @Column(name = "qr_code", columnDefinition = "LONGTEXT")
    private String qrCode;
}
