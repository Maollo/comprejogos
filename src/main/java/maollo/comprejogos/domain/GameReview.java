package maollo.comprejogos.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "game_reviews", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "game_appid"})
})
public class GameReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserCompreJogos user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_appid", referencedColumnName = "appId", nullable = false)
    private Game game;

    @Column(nullable = false)
    private int rating; // Nota (ex: 1 a 5)

    @Lob
    private String comment;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime reviewDate;
}