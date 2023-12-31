package com.example.finalproject.repository.category;

import com.example.finalproject.domain.entity.category.CategoryEntity;
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
public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {
    Boolean existsByType(String type);
    List<CategoryEntity> findByParentIdNotNull();

    @Query("select c from category c where c.parent.id = null")
    List<CategoryEntity> findByParentCategory();

    @Query("select c from category c where c.type = :type")
    Optional<CategoryEntity> findByType (@Param("type") String category);

    List<CategoryEntity> findCategoryEntityByParentId(UUID uuid);

    @Query(value = "SELECT c FROM category c WHERE (LOWER(c.type) LIKE CONCAT('%', LOWER(:keyword), '%')) AND c.parent IS NULL")
    List<CategoryEntity> findByKeyword(@Param("keyword") String keyword);

    @Query("SELECT c FROM category c WHERE LOWER(c.type) = LOWER(:keyword) AND c.parent = :parentObj")
    List<CategoryEntity> searchKeywordForChild(@Param("keyword") String keyword, @Param("parentObj") CategoryEntity parentObj);
    @Transactional
    @Modifying
    @Query("update category c set c.type = :type where c.id = :id")
    void updateCategoryType(@Param("type") String type, @Param("id") UUID id);
}
