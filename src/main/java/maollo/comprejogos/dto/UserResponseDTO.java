package maollo.comprejogos.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserResponseDTO {
    private Integer id;
    private String login;
    private String email;
    private String avatarUrl;
    private LocalDateTime createdAt;
}