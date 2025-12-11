package mx.edu.utez.back.model;

import lombok.*;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "main_store_id")
    private Long mainStoreId;

    @ManyToOne
    @JoinColumn(name = "main_store_id", insertable = false, updatable = false)
    private Store mainStore;

    @Column(name = "assigned_store_ids", columnDefinition = "JSON")
    private String assignedStoreIds;

    @Column(name = "fcm_token")
    private String fcmToken;
}
