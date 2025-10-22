package maollo.comprejogos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemRequestDTO {
    @NotNull(message = "O App ID do jogo não pode ser nulo.")
    private Long gameAppId;

    @Min(value = 1, message = "A quantidade deve ser de no mínimo 1.")
    private int quantity = 1;
}