package maollo.comprejogos.repository;

import maollo.comprejogos.domain.Category;
import maollo.comprejogos.domain.Game;
import maollo.comprejogos.domain.GameReview;
import maollo.comprejogos.domain.UserCompreJogos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameReviewRepository extends JpaRepository<GameReview, Long> {
    List<GameReview> findByGame(Game game);

    List<GameReview> findByUser(UserCompreJogos user);
}
