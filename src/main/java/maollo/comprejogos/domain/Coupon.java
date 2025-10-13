package maollo.comprejogos.domain;

import jakarta.persistence.*;
import lombok.Data;
import maollo.comprejogos.utils.CouponType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, length = 50)
    private String code;

    @Enumerated(EnumType.STRING)
    private CouponType type;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal value;

    private Integer maxUses;
    private Integer currentUses = 0;
    private LocalDate validUntil;
    private boolean active = true;
}

