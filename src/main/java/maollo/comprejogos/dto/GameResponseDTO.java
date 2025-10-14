package maollo.comprejogos.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data

public class GameResponseDTO {
    private Long appId;
    private String name;
    private BigDecimal price;
    private BigDecimal promotionalPrice;
    private String description;
    private String developer;
    private String publisher;
    private LocalDate releaseDate;
    private String imageUrl;
    private String trailerUrl;
    private String coverUrl;
    private String tags;
}