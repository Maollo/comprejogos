package maollo.comprejogos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "steam_apps")
public class SteamApp {

    /**
     * O ID do aplicativo da Steam.
     * Note que não usamos @GeneratedValue, pois este ID é fornecido pela Steam.
     */
    @Id
    private Integer appid;

    @Column(nullable = false)
    private String name;

    @Column(name = "name_norm", nullable = false)
    private String normalizedName;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
