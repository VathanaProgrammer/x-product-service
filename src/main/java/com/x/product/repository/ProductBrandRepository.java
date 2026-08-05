package com.x.product.repository;

import com.x.product.entity.ProductBrand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductBrandRepository extends JpaRepository<ProductBrand, Long> {
    Page<ProductBrand> findByBusinessIdAndStatusNot(Long businessId, String status, Pageable pageable);
    Page<ProductBrand> findDistinctByBusinessIdAndStoreIdsContainingAndStatusNot(Long businessId, Long storeId, String status, Pageable pageable);
    Optional<ProductBrand> findByIdAndBusinessIdAndStatusNot(Long id, Long businessId, String status);
    boolean existsByBusinessIdAndBrandCodeIgnoreCaseAndStatusNot(Long businessId, String brandCode, String status);
    boolean existsByBusinessIdAndBrandCodeIgnoreCaseAndIdNotAndStatusNot(Long businessId, String brandCode, Long id, String status);
}
