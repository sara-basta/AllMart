package com.sara.allmart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class OrderItem {
    @Id
    @SequenceGenerator(
            name = "order_items_id_seq",
            sequenceName = "order_items_id_seq",
            allocationSize = 10
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "order_items_id_seq"
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name="order_id",
            foreignKey = @ForeignKey(name = "fk_order_items_orders")
    )
    @JsonIgnore
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name="product_id",
            foreignKey = @ForeignKey(name = "fk_order_items_products")
    )
    private Product product;

    private Integer quantity;
    private BigDecimal priceAtPurchase;

}
