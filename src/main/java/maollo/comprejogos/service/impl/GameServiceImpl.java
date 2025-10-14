package maollo.comprejogos.service.impl;

import lombok.RequiredArgsConstructor;
import maollo.comprejogos.domain.Game;
import maollo.comprejogos.dto.GameRequestDTO;
import maollo.comprejogos.dto.GameResponseDTO;
import maollo.comprejogos.exception.ResourceNotFoundException;
import maollo.comprejogos.mapper.GameMapper;
import maollo.comprejogos.repository.GameRepository;
import maollo.comprejogos.service.GameService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final GameMapper gameMapper; // Injetar o mapper



    @Override
    public Optional<Game> findByAppId(Long appId) {
        return gameRepository.findByAppId(appId);
    }

    @Override
    public List<Game> findFeaturedGames() {
        return gameRepository.findByFeatured(true);
    }

    @Override
    public Game saveGame(Game game) {
        // Poderia ter lógicas aqui, como verificar se o AppId já existe antes de salvar
        return gameRepository.save(game);
    }

    @Override
    public List<GameResponseDTO> findAllGames() {
        return gameRepository.findAll().stream()
                .map(gameMapper::toGameResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GameResponseDTO createGame(GameRequestDTO gameDto) {
        if (gameRepository.findByAppId(gameDto.getAppId()).isPresent()) {
            throw new IllegalStateException("Um jogo com o AppId " + gameDto.getAppId() + " já existe.");
        }
        Game game = gameMapper.toGame(gameDto);
        Game savedGame = gameRepository.save(game);
        return gameMapper.toGameResponseDTO(savedGame);
    }

    @Override
    public GameResponseDTO updateGame(Long appId, GameRequestDTO gameDto) {
        Game gameToUpdate = gameRepository.findByAppId(appId)
                .orElseThrow(() -> new ResourceNotFoundException("Jogo não encontrado com o AppId: " + appId));

        gameMapper.updateGameFromDto(gameDto, gameToUpdate);
        Game updatedGame = gameRepository.save(gameToUpdate);
        return gameMapper.toGameResponseDTO(updatedGame);
    }

    @Override
    public void deleteGame(Long appId) {
        Game gameToDelete = gameRepository.findByAppId(appId)
                .orElseThrow(() -> new ResourceNotFoundException("Jogo não encontrado com o AppId: " + appId));
        gameRepository.delete(gameToDelete);
    }



}