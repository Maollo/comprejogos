package maollo.comprejogos.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import maollo.comprejogos.utils.ERole;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Entity
@Builder
@Table(name = "users") // Mapeando para a tabela 'usuarios' com nome em inglês
public class UserCompreJogos implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

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


    @ManyToMany(fetch = FetchType.EAGER) // EAGER para carregar as roles junto com o usuário
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
