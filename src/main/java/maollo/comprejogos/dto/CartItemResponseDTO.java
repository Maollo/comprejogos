package maollo.comprejogos.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartItemResponseDTO {
    private Long gameAppId;
    private String gameName;
    private String gameImageUrl;
    private int quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
}