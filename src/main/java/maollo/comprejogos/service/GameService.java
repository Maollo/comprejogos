package maollo.comprejogos.service;

import maollo.comprejogos.domain.Game;
import maollo.comprejogos.dto.GameRequestDTO;
import maollo.comprejogos.dto.GameResponseDTO;

import java.util.List;
import java.util.Optional;

public interface GameService {
    List<GameResponseDTO> findAllGames();
    Optional<Game> findByAppId(Long appId);
    List<Game> findFeaturedGames();
    Game saveGame(Game game);
    // --- NOVOS MÉTODOS ---
    GameResponseDTO createGame(GameRequestDTO gameDto);
    GameResponseDTO updateGame(Long appId, GameRequestDTO gameDto);
    void deleteGame(Long appId);
}