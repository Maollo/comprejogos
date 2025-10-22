package maollo.comprejogos.dto;

import maollo.comprejogos.utils.OrderStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class OrderResponseDTO {
    private Long id;
    private OrderStatus status;
    private BigDecimal total;
    private LocalDateTime createdAt;
    private Set<OrderItemResponseDTO> items;
}