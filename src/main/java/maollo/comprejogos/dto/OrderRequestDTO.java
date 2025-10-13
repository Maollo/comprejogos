package maollo.comprejogos.dto;

import java.util.List;
import lombok.Data;

@Data // Lombok para gerar getters e setters
public class OrderRequestDTO {
    private Long userId;
    private List<Long> gameAppIds;
}