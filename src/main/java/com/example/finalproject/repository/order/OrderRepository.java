package com.example.finalproject.repository.order;

import com.example.finalproject.domain.entity.order.OrderEntity;
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
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
    @Query("select o from orders o where o.user.chatId = :chatId and o.orderState = 'IN_CART'")
    List<OrderEntity> findByChatId(@Param("chatId") Long chatId);

    @Modifying
    @Transactional
    @Query("update orders o set o.amount = :amount where o.user.id = :userId and o.product.id = :productId and o.orderState = 'IN_CART'")
    void updateAmount(@Param("amount") Integer amount, @Param("userId") UUID userId, @Param("productId") UUID productId);

    @Modifying
    @Transactional
    @Query("update orders o set o.amount = :amount where o.user.id = :userId and o.product.id = :productId and o.orderState = 'ORDERED'")
    void updateOrderAmountByStateEqualsOrdered(@Param("amount") Integer amount, @Param("userId") UUID userId, @Param("productId") UUID productId);

    @Modifying
    @Transactional
    @Query("update orders o set o.amount = o.amount + 1 where o.id = :id")
    void plusOrderAmount(@Param("id") UUID uuid);

    @Modifying
    @Transactional
    @Query("update orders o set o.amount = o.amount - 1 where o.id = :id")
    void minusOrderAmount(@Param("id") UUID uuid);

    @Query("select o from orders o where o.orderState <> 'IN_CART'")
    List<OrderEntity> findAllByState();

    @Modifying
    @Transactional
    @Query("update orders o set o.orderState = 'ORDERED' where o.id = :id")
    void updateOrderState(@Param("id") UUID id);

    @Query("select o from orders o where o.user.id = :userId and o.product.id = :productId and o.orderState = 'IN_CART'")
    Optional<OrderEntity> findOrderEntityByUserIdAndProductId(@Param("userId") UUID userId, @Param("productId") UUID productId);

    @Query("select o from orders o where o.user.id = :userId and o.product.id = :productId and o.orderState = 'ORDERED'")
    Optional<OrderEntity> findOrderEntityByStateEqualsOrdered(@Param("userId") UUID userId, @Param("productId") UUID productId);

}
