package maollo.comprejogos.controller;

import maollo.comprejogos.domain.Game;
import maollo.comprejogos.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Endpoint para listar todos os jogos.
     * HTTP GET para /api/games
     * @return Uma lista de todos os jogos com status 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<Game>> getAllGames() {
        List<Game> games = gameService.findAllGames();
        return ResponseEntity.ok(games);
    }
    
    /**
     * Endpoint para listar os jogos em destaque.
     * HTTP GET para /api/games/featured
     * @return Uma lista dos jogos em destaque com status 200 (OK).
     */
    @GetMapping("/featured")
    public ResponseEntity<List<Game>> getFeaturedGames() {
        List<Game> featuredGames = gameService.findFeaturedGames();
        return ResponseEntity.ok(featuredGames);
    }

    /**
     * Endpoint para buscar um jogo específico pelo seu App ID.
     * HTTP GET para /api/games/{appId}
     * @param appId O App ID do jogo, vindo da URL.
     * @return O jogo encontrado com status 200 (OK) ou 404 (Not Found) se não existir.
     */
    @GetMapping("/{appId}")
    public ResponseEntity<Game> getGameByAppId(@PathVariable Long appId) {
        return gameService.findByAppId(appId)
                .map(ResponseEntity::ok) // Forma abreviada de: .map(game -> ResponseEntity.ok(game))
                .orElse(ResponseEntity.notFound().build());
    }
}