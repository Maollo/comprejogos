package maollo.comprejogos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class GameRequestDTO {

    @NotNull(message = "O App ID não pode ser nulo.")
    private Long appId;

    @NotBlank(message = "O nome do jogo não pode ser vazio.")
    @Size(max = 255, message = "O nome do jogo deve ter no máximo 255 caracteres.")
    private String name;

    @NotNull(message = "O preço não pode ser nulo.")
    @PositiveOrZero(message = "O preço deve ser um valor positivo ou zero.")
    private BigDecimal price;

    @PositiveOrZero(message = "O preço promocional deve ser um valor positivo ou zero.")
    private BigDecimal promotionalPrice;

    private String description;
    private String developer;
    private String publisher;
    private LocalDate releaseDate;
    private String imageUrl;
    private String trailerUrl;
    private String coverUrl;
    private String tags;
    private boolean featured = false;
}