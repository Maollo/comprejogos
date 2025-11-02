package maollo.comprejogos.repository;

import maollo.comprejogos.domain.Category;
import maollo.comprejogos.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findByAppId(Long appId);


    List<Game> findByNameContainingIgnoreCase(String name);


    List<Game> findByFeatured(boolean featured);

    /**
     * Busca jogos paginados, permitindo filtrar por nome (contendo, case-insensitive)
     * e por uma tag/categoria (contendo, case-insensitive).
     * Se searchTerm ou tag for nulo/vazio, o filtro correspondente é ignorado.
     *
     * @param searchTerm O termo a ser buscado no nome do jogo.
     * @param tag A tag/categoria para filtrar (busca dentro do campo 'tags').
     * @param pageable Objeto de paginação e ordenação.
     * @return Uma página de jogos que correspondem aos critérios.
     */
    @Query("SELECT g FROM Game g WHERE " +
            "(:searchTerm IS NULL OR LOWER(g.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
            // REMOVA O LOWER() DAQUI:
            "(:tag IS NULL OR g.tags LIKE CONCAT('%', :tag, '%')) AND " +
            "g.active = true")
    Page<Game> findGamesByCriteria( // <-- NOME ALTERADO AQUI
                                    @Param("searchTerm") String searchTerm,
                                    @Param("tag") String tag,
                                    Pageable pageable);

}
