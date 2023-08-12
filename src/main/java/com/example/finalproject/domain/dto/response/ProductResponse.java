package com.example.finalproject.domain.dto.response;

import com.example.finalproject.domain.entity.category.CategoryEntity;
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
public class ProductResponse {
    private UUID id;
    private String name;
    private Double price;
    private Integer amount;
    private CategoryEntity category;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String lastModifiedBy;
    private String createdBy;
}

