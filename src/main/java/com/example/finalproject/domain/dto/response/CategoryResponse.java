package com.example.finalproject.domain.dto.response;

import com.example.finalproject.domain.entity.category.CategoryEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponse {
    private String type;
    private CategoryEntity parentId;
    private UUID id;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String lastModifiedBy;
    private String createdBy;
}
