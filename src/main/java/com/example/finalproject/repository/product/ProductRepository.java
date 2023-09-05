package com.example.finalproject.repository.product;

import com.example.finalproject.domain.entity.product.ProductEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
    List<ProductEntity> findProductEntityByCategory_Id (UUID id);
    Optional<ProductEntity> findProductEntityByName (String name);
//d
    @Modifying
    @Transactional
    @Query("update products p set p.amount = p.amount - :amount where p.id = :id")
    void updateProductAmount(@Param("amount") Integer amount, @Param("id") UUID id);

    @Modifying
    @Transactional
    @Query("update products p set p.amount = p.amount + :amount where p.id = :id")
    void plusProductAmount(@Param("amount") Integer amount, @Param("id") UUID id);

    List<ProductEntity> getProductEntitiesByCategory_Id(UUID categoryId);
    Boolean existsByName(String name);

    @Query(value = "SELECT p FROM products p WHERE LOWER(p.name) LIKE CONCAT('%', LOWER(:keyword), '%')")
    List<ProductEntity> findByKeyword(@Param("keyword") String keyword);

}
