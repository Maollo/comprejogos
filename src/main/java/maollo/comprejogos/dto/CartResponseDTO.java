package maollo.comprejogos.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Set;

@Data
public class CartResponseDTO {
    private Long id;
    private Set<CartItemResponseDTO> items;
    private BigDecimal total;
}