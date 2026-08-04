package com.x.product.repository;

import com.x.product.entity.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    Page<ProductCategory> findByBusinessIdAndStatusNot(
            Long businessId, String status, Pageable pageable);

    Page<ProductCategory> findDistinctByBusinessIdAndStoreIdsContainingAndStatusNot(
            Long businessId, Long storeId, String status, Pageable pageable);

    Optional<ProductCategory> findByIdAndBusinessIdAndStatusNot(
            Long id, Long businessId, String status);

    boolean existsByBusinessIdAndCategoryCodeIgnoreCaseAndStatusNot(
            Long businessId, String categoryCode, String status);

    boolean existsByBusinessIdAndCategoryCodeIgnoreCaseAndIdNotAndStatusNot(
            Long businessId, String categoryCode, Long id, String status);
}
