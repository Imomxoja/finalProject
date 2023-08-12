package com.example.finalproject.domain.dto.request;

import com.example.finalproject.domain.entity.order.OrderStatus;
import com.example.finalproject.domain.entity.product.ProductEntity;
import com.example.finalproject.domain.entity.user.UserEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {
    private UserEntity user;
    private Integer amount;
    private OrderStatus orderState;
    private ProductEntity product;
}