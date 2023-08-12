package com.example.finalproject.domain.entity.history;

import com.example.finalproject.domain.entity.BaseEntity;
import com.example.finalproject.domain.entity.user.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity(name = "histories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistoryEntity extends BaseEntity {
    private String productName;
    private Double price;
    private Integer amount;
    private Double totalPrice;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
