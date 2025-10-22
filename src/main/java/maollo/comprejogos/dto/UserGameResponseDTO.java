package maollo.comprejogos.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserGameResponseDTO {

    private Long gameAppId;
    private String gameName;
    private String gameImageUrl;
    private String gameDeveloper;
    private LocalDate gameReleaseDate;

    private BigDecimal purchasePrice;
    private LocalDateTime purchaseDate;
}