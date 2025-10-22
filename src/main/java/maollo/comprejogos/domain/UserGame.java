package maollo.comprejogos.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_games", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "game_appid"}) // Impede o usuário de ter o mesmo jogo duas vezes
})
@Getter
@Setter
public class UserGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserCompreJogos user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_appid", referencedColumnName = "appId", nullable = false)
    private Game game;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal purchasePrice;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime purchaseDate;
}