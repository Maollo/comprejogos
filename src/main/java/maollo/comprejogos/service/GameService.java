package maollo.comprejogos.service;

import maollo.comprejogos.domain.Game;
import java.util.List;
import java.util.Optional;

public interface GameService {
    List<Game> findAllGames();
    Optional<Game> findByAppId(Long appId);
    List<Game> findFeaturedGames();
    Game saveGame(Game game);
}