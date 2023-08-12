package com.example.finalproject.domain.entity.category;

import com.example.finalproject.domain.entity.BaseEntity;
import com.example.finalproject.domain.entity.product.ProductEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "category")
@Builder
public class CategoryEntity extends BaseEntity {
    @Column(unique = true)
    private String type;
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CategoryEntity> children;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private CategoryEntity parent;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductEntity> products;
}