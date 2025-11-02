package maollo.comprejogos.service;

import maollo.comprejogos.domain.Game;
import maollo.comprejogos.dto.GameRequestDTO;
import maollo.comprejogos.dto.GameResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface GameService {
    Page<GameResponseDTO> findAvailableGames(String searchTerm, String tag, Pageable pageable);
    Page<GameResponseDTO> findAllGames(Pageable pageable);
    Optional<Game> findByAppId(Long appId);
    List<Game> findFeaturedGames();
    Game saveGame(Game game);
    // --- NOVOS MÉTODOS ---
    GameResponseDTO createGame(GameRequestDTO gameDto);
    GameResponseDTO updateGame(Long appId, GameRequestDTO gameDto);
    void deleteGame(Long appId);
}