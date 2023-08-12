package com.example.finalproject.domain.entity.product;

import com.example.finalproject.domain.entity.BaseEntity;
import com.example.finalproject.domain.entity.category.CategoryEntity;
import com.example.finalproject.domain.entity.order.OrderEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "products")
@Builder
public class ProductEntity extends BaseEntity {
    @Column(unique = true)
    private String name;
    private Double price;
    private Integer amount;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;
    @OneToMany(mappedBy = "product")
    private List<OrderEntity> order;
}
