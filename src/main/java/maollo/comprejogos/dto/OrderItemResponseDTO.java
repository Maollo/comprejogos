package maollo.comprejogos.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemResponseDTO {
    private Long gameAppId;
    private String gameName;
    private int quantity;
    private BigDecimal price;
}