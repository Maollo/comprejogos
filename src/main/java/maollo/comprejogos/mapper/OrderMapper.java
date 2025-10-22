package maollo.comprejogos.mapper;

import maollo.comprejogos.dto.OrderItemResponseDTO;
import maollo.comprejogos.dto.OrderResponseDTO;
import maollo.comprejogos.domain.Order;
import maollo.comprejogos.domain.OrderItem;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderResponseDTO toOrderResponseDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(Long.valueOf(order.getId()));
        dto.setStatus(order.getStatus());
        dto.setTotal(order.getTotal());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setItems(order.getItems().stream()
                .map(this::toOrderItemResponseDTO)
                .collect(Collectors.toSet()));
        return dto;
    }

    public OrderItemResponseDTO toOrderItemResponseDTO(OrderItem item) {
        OrderItemResponseDTO dto = new OrderItemResponseDTO();
        dto.setPrice(item.getPrice());
        dto.setQuantity(item.getQuantity());
        if (item.getGame() != null) {
            dto.setGameAppId(item.getGame().getAppId());
            dto.setGameName(item.getGame().getName());
        }
        return dto;
    }
}