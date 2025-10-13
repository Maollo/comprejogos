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
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private Long appId;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price = BigDecimal.ZERO;

    @Column(precision = 10, scale = 2)
    private BigDecimal promotionalPrice;

    @Lob
    private String description;

    @Column(length = 255)
    private String developer;

    @Column(length = 255)
    private String publisher;

    private LocalDate releaseDate;

    private boolean active = true;
    private boolean featured = false;
    private boolean promotionActive = false;
    private LocalDate promotionUntil;

    @Column(length = 500)
    private String imageUrl;

    @Column(length = 500)
    private String trailerUrl;

    @Column(length = 500)
    private String coverUrl;

    @Lob
    private String minimumRequirements;

    @Lob
    private String recommendedRequirements;

    @Lob
    private String tags;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}