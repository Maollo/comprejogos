package maollo.comprejogos.repository;

import maollo.comprejogos.domain.Category;
import maollo.comprejogos.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findByAppId(Long appId);


    List<Game> findByNameContainingIgnoreCase(String name);


    List<Game> findByFeatured(boolean featured);
}
