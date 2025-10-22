package maollo.comprejogos.mapper;

import maollo.comprejogos.domain.Game;
import maollo.comprejogos.domain.UserGame;
import maollo.comprejogos.dto.UserGameResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class UserGameMapper {

    public UserGameResponseDTO toUserGameResponseDTO(UserGame userGame) {
        if (userGame == null) {
            return null;
        }

        UserGameResponseDTO dto = new UserGameResponseDTO();
        dto.setPurchasePrice(userGame.getPurchasePrice());
        dto.setPurchaseDate(userGame.getPurchaseDate());

        Game game = userGame.getGame();
        if (game != null) {
            dto.setGameAppId(game.getAppId());
            dto.setGameName(game.getName());
            dto.setGameImageUrl(game.getImageUrl());
            dto.setGameDeveloper(game.getDeveloper());
            dto.setGameReleaseDate(game.getReleaseDate());
        }

        return dto;
    }
}