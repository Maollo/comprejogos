package maollo.comprejogos.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import maollo.comprejogos.utils.OrderStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private UserCompreJogos user;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private OrderStatus status = OrderStatus.PENDENTE;

    @Column(length = 50) // Ex: 'PIX', 'CREDIT_CARD', 'BOLETO'
    private String paymentMethod;

    @Column(length = 255, unique = true, nullable = true)
    // ID único do pagamento no gateway (ex: ID da preferência do Mercado Pago)
    private String paymentGatewayReference;

    @Column(columnDefinition = "TEXT") // Pode armazenar a URL de pagamento, QR Code data, etc.
    private String paymentDetails;

    private LocalDateTime paymentConfirmedAt; // Quando o webhook confirmou o pagamento

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> items = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

