package mx.edu.utez.back.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "temporary_assignments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemporaryAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @ManyToOne
    @JoinColumn(name = "store_id", insertable = false, updatable = false)
    private Store store;

    @Column(name = "repartidor_id", nullable = false)
    private Long repartidorId;

    @ManyToOne
    @JoinColumn(name = "repartidor_id", insertable = false, updatable = false)
    private User repartidor;

    @Column(nullable = false)
    private LocalDate date;
}
