package com.sara.allmart.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Cart extends BaseEntity{
    @Id
    @SequenceGenerator(
            name = "cart_id_seq",
            sequenceName = "cart_id_seq",
            allocationSize = 10
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cart_id_seq"
    )
    private Long id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @NotNull(message = "User is required")
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @PositiveOrZero
    private BigDecimal totalPrice = BigDecimal.ZERO;

    public void calculateTotal() {
        if (items == null || items.isEmpty()) {
            this.totalPrice = BigDecimal.ZERO;
            return;
        }

        this.totalPrice = items.stream()
                .map(item -> {
                    BigDecimal price = item.getPrice();
                    BigDecimal qty = BigDecimal.valueOf(item.getQuantity());
                    return price.multiply(qty);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
