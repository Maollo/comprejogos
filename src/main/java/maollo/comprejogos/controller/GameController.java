package maollo.comprejogos.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import maollo.comprejogos.domain.Game;
import maollo.comprejogos.dto.GameRequestDTO;
import maollo.comprejogos.dto.GameResponseDTO;
import maollo.comprejogos.mapper.GameMapper;
import maollo.comprejogos.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault; // Opcional, para definir um padrão
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final GameMapper gameMapper;

    @GetMapping
    public ResponseEntity<Page<GameResponseDTO>> getAvailableGames(
            // 1. Adicione os @RequestParam para busca e filtro
            @RequestParam(required = false) String search, // 'search' será o nome do parâmetro na URL
            @RequestParam(required = false) String tag,    // 'tag' será o nome do parâmetro na URL

            // Mantenha o Pageable
            @PageableDefault(size = 12, sort = "name") Pageable pageable
    ) {
        // 2. Passe os parâmetros para o serviço
        Page<GameResponseDTO> gamePage = gameService.findAvailableGames(search, tag, pageable);
        return ResponseEntity.ok(gamePage);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<GameResponseDTO>> getAllGames(@PageableDefault(size = 12, sort = "name") Pageable pageable) {
        Page<GameResponseDTO> games = gameService.findAllGames(pageable);
        return ResponseEntity.ok(games);
    }

    /**
     * Endpoint para listar os jogos em destaque.
     * HTTP GET para /api/games/featured
     *
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
     *
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
    // --- MÉTODOS CRUD (Admin) ---

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GameResponseDTO> createGame(@Valid @RequestBody GameRequestDTO gameDto) {
        GameResponseDTO createdGame = gameService.createGame(gameDto);
        return new ResponseEntity<>(createdGame, HttpStatus.CREATED);
    }

    @PutMapping("/{appId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GameResponseDTO> updateGame(@PathVariable Long appId, @Valid @RequestBody GameRequestDTO gameDto) {
        GameResponseDTO updatedGame = gameService.updateGame(appId, gameDto);
        return ResponseEntity.ok(updatedGame);
    }

    @DeleteMapping("/{appId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteGame(@PathVariable Long appId) {
        gameService.deleteGame(appId);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content, que é o padrão para delete bem-sucedido
    }
}