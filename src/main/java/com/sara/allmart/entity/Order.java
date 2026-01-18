package com.sara.allmart.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @SequenceGenerator(
            name = "order_id_seq",
            sequenceName = "order_id_seq",
            allocationSize = 10
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "order_id_seq"
    )
    private Long id;
    private OrderStatus status;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "shipping_street")),
            @AttributeOverride(name = "city", column = @Column(name = "shipping_city")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "shipping_zip"))
    })
    private Address shippingAddress;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> items = new HashSet<>();
    private BigDecimal totalAmount;
}
