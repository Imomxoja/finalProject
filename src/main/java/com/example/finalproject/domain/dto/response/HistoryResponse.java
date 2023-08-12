package com.example.finalproject.domain.dto.response;

import com.example.finalproject.domain.entity.user.UserEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class HistoryResponse {
    private String productName;
    private Double price;
    private Integer amount;
    private Double totalPrice;
    private UserEntity user;
    private UUID id;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String lastModifiedBy;
    private String createdBy;
}
