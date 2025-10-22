package maollo.comprejogos.repository;

import maollo.comprejogos.domain.Category;
import maollo.comprejogos.domain.Order;
import maollo.comprejogos.domain.UserCompreJogos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(UserCompreJogos user);
    List<Order> findByUserOrderByCreatedAtDesc(UserCompreJogos user);
}
