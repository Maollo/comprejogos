package maollo.comprejogos.mapper;

import maollo.comprejogos.dto.UserResponseDTO;
import maollo.comprejogos.domain.UserCompreJogos;
import org.springframework.stereotype.Component;

@Component // Torna a classe um bean gerenciado pelo Spring para que possamos injetá-la
public class UserMapper {

    public UserResponseDTO toUserResponseDTO(UserCompreJogos user) {
        if (user == null) {
            return null;
        }
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setLogin(user.getLogin());
        dto.setEmail(user.getEmail());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}