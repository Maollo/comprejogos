package maollo.comprejogos.repository;

import maollo.comprejogos.domain.Game;
import maollo.comprejogos.domain.UserCompreJogos;
import maollo.comprejogos.domain.UserGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserGameRepository extends JpaRepository<UserGame, Long> {
    List<UserGame> findByUser(UserCompreJogos user);
    Optional<UserGame> findByUserAndGame(UserCompreJogos user, Game game);
}