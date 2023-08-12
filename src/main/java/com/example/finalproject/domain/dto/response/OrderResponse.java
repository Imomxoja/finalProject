package com.example.finalproject.domain.dto.response;

import com.example.finalproject.domain.entity.order.OrderStatus;
import com.example.finalproject.domain.entity.product.ProductEntity;
import com.example.finalproject.domain.entity.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private UserEntity user;
    private OrderStatus orderState;
    private Integer amount;
    private ProductEntity product;
    private UUID id;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String lastModifiedBy;
    private String createdBy;
}
