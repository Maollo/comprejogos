package maollo.comprejogos.controller;

import lombok.RequiredArgsConstructor;
import maollo.comprejogos.domain.Game;
import maollo.comprejogos.dto.GameResponseDTO;
import maollo.comprejogos.mapper.GameMapper;
import maollo.comprejogos.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final GameMapper gameMapper;

    /**
     * Endpoint para listar todos os jogos.
     * HTTP GET para /api/games
     * @return Uma lista de todos os jogos com status 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<GameResponseDTO>> getAllGames() {
        List<Game> games = gameService.findAllGames();
        List<GameResponseDTO> list = games.stream().map(gameMapper::toGameResponseDTO).toList();
        return ResponseEntity.ok(list);
    }
    
    /**
     * Endpoint para listar os jogos em destaque.
     * HTTP GET para /api/games/featured
     * @return Uma lista dos jogos em destaque com status 200 (OK).
     */
    @GetMapping("/featured")
    public ResponseEntity<List<GameResponseDTO>> getFeaturedGames() {
        List<Game> featuredGames = gameService.findFeaturedGames();
        List<GameResponseDTO> list = featuredGames.stream().map(gameMapper::toGameResponseDTO).toList();
        return ResponseEntity.ok(list);
    }

    /**
     * Endpoint para buscar um jogo específico pelo seu App ID.
     * HTTP GET para /api/games/{appId}
     * @param appId O App ID do jogo, vindo da URL.
     * @return O jogo encontrado com status 200 (OK) ou 404 (Not Found) se não existir.
     */
    @GetMapping("/{appId}")
    public ResponseEntity<GameResponseDTO> getGameByAppId(@PathVariable Long appId) {
        return gameService.findByAppId(appId)
                .map(gameMapper::toGameResponseDTO)
                .map(ResponseEntity::ok) // Forma abreviada de: .map(game -> ResponseEntity.ok(game))
                .orElse(ResponseEntity.notFound().build());
    }
}