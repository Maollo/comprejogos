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
        dto.setPromotionalPrice(game.getPromotionalPrice());
        dto.setImageUrl(game.getImageUrl());
        dto.setTrailerUrl(game.getTrailerUrl());
        dto.setCoverUrl(game.getCoverUrl());
        dto.setDeveloper(game.getDeveloper());
        dto.setPublisher(game.getPublisher());
        dto.setReleaseDate(game.getReleaseDate());

        // CAMPOS IMPORTANTES PARA A PÁGINA DE DETALHES
        dto.setDescription(game.getDescription());
        dto.setMinimumRequirements(game.getMinimumRequirements());
        dto.setRecommendedRequirements(game.getRecommendedRequirements());
        dto.setTags(game.getTags());

        return dto;
    }

    public Game toGame(GameRequestDTO dto) {
        if (dto == null) return null;
        Game game = new Game();
        game.setAppId(dto.getAppId());
        game.setName(dto.getName());
        game.setPrice(dto.getPrice());
        game.setImageUrl(dto.getImageUrl());
        game.setPromotionalPrice(dto.getPromotionalPrice());
        game.setDeveloper(dto.getDeveloper());
        game.setCoverUrl(dto.getCoverUrl());
        game.setReleaseDate(dto.getReleaseDate());
        game.setTags(dto.getTags());
        game.setTrailerUrl(dto.getTrailerUrl());

        return game;
    }

    public void updateGameFromDto(GameRequestDTO dto, Game game) {
        if (dto == null || game == null) return;
        game.setName(dto.getName());
        game.setPrice(dto.getPrice());

    }
}