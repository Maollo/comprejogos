package maollo.comprejogos.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users") // Mapeando para a tabela 'usuarios' com nome em inglês
public class UserCompreJogos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String login;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    private boolean emailVerified = false;
    private LocalDateTime emailVerifiedAt;
    private boolean newsletterSubscribed = true;

    @Column(length = 255, nullable = false)
    private String password;

    private boolean active = true;
    private boolean isClient = false;
    private boolean isAdmin = false;

    @Column(precision = 10, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    private boolean twoFactorEnabled = false;
    private String twoFactorSecret;

    @Column(length = 500)
    private String avatarUrl;

    private LocalDate birthDate;

    @Column(length = 20)
    private String phone;

    @Column(length = 100)
    private String country;

    @Column(length = 100)
    private String city;

    private LocalDateTime lastActivity;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalSpent = BigDecimal.ZERO;

    private int totalGames = 0;
    private int userLevel = 1;
    private int loyaltyPoints = 0;

    @Lob // Para campos de texto mais longos
    private String emailPreferences;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
