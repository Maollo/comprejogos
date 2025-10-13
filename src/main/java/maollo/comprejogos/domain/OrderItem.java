package maollo.comprejogos.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_appid", referencedColumnName = "appId", nullable = false)
    private Game game;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    private int quantity = 1;
}