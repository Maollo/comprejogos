package maollo.comprejogos.mapper;

import maollo.comprejogos.domain.Game;
import maollo.comprejogos.dto.GameResponseDTO;
import maollo.comprejogos.dto.UserResponseDTO;
import maollo.comprejogos.domain.UserCompreJogos;
import org.springframework.stereotype.Component;

@Component // Torna a classe um bean gerenciado pelo Spring para que possamos injetá-la
public class GameMapper {

    public GameResponseDTO toGameResponseDTO(Game game) {
        if (game == null) {
            return null;
        }

        return GameResponseDTO.builder().appId(game.getAppId())
                .name(game.getName()).description(game.getDescription()).price(game.getPrice())
                .promotionalPrice(game.getPromotionalPrice()).tags(game.getTags()).developer(game.getDeveloper())
                .imageUrl(game.getImageUrl()).coverUrl(game.getCoverUrl())
                .trailerUrl(game.getTrailerUrl()).releaseDate(game.getReleaseDate()).publisher(game.getPublisher()).build();
    }
}