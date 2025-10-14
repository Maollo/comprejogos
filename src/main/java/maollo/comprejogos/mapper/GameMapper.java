package maollo.comprejogos.mapper;

import maollo.comprejogos.dto.GameRequestDTO;
import maollo.comprejogos.dto.GameResponseDTO;
import maollo.comprejogos.domain.Game;
import org.springframework.stereotype.Component;

@Component
public class GameMapper {

    public GameResponseDTO toGameResponseDTO(Game game) {
        if (game == null) return null;
        GameResponseDTO dto = GameResponseDTO.builder().build();
        dto.setAppId(game.getAppId());
        dto.setName(game.getName());
        dto.setPrice(game.getPrice());
        // ... mapeie todos os outros campos de Game para GameResponseDTO
        return dto;
    }

    public Game toGame(GameRequestDTO dto) {
        if (dto == null) return null;
        Game game = new Game();
        game.setAppId(dto.getAppId());
        game.setName(dto.getName());
        game.setPrice(dto.getPrice());
        // ... mapeie todos os outros campos de GameRequestDTO para Game
        return game;
    }

    public void updateGameFromDto(GameRequestDTO dto, Game game) {
        if (dto == null || game == null) return;
        game.setName(dto.getName());
        game.setPrice(dto.getPrice());
        // ... mapeie todos os outros campos, mas NÃO o appId ou id
    }
}