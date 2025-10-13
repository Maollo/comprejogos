package maollo.comprejogos.repository;

import maollo.comprejogos.domain.Category;
import maollo.comprejogos.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
