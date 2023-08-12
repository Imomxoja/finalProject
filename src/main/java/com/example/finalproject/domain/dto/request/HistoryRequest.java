package com.example.finalproject.domain.dto.request;

import com.example.finalproject.domain.entity.user.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class HistoryRequest {
    private String productName;
    private Double price;
    private Integer amount;
    private Double totalPrice;
    private UserEntity user;
}
