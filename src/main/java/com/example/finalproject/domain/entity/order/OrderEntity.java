package com.example.finalproject.domain.entity.order;

import com.example.finalproject.domain.entity.BaseEntity;
import com.example.finalproject.domain.entity.product.ProductEntity;
import com.example.finalproject.domain.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "orders")
@Builder
public class OrderEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    private Integer amount;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderState;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;
}

