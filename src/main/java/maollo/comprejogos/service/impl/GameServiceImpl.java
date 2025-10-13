package maollo.comprejogos.service.impl;

import maollo.comprejogos.domain.Game;
import maollo.comprejogos.repository.GameRepository;
import maollo.comprejogos.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public List<Game> findAllGames() {
        return gameRepository.findAll();
    }

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
}