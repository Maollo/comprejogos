package maollo.comprejogos.repository;

import maollo.comprejogos.domain.Category;
import maollo.comprejogos.domain.UserCompreJogos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCompreJogosRepository extends JpaRepository<UserCompreJogos, Long> {


        Optional<UserCompreJogos> findByLogin(String login);

        Optional<UserCompreJogos> findByEmail(String email);

}