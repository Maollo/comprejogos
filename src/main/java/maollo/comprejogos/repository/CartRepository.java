package maollo.comprejogos.repository;

import maollo.comprejogos.domain.Cart;
import maollo.comprejogos.domain.UserCompreJogos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(UserCompreJogos user);

    Optional<Cart> findByUserId(Integer userId);
}