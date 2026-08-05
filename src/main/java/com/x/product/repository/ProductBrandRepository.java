package com.x.product.repository;

import com.x.product.entity.ProductBrand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductBrandRepository extends JpaRepository<ProductBrand, Long> {

    Page<ProductBrand> findByBusinessIdAndStatusNot(Long businessId, String status, Pageable pageable);

    @Query("""
        SELECT DISTINCT b FROM ProductBrand b
        LEFT JOIN b.storeIds s
        WHERE b.businessId = :businessId
          AND b.status != :status
          AND (b.isGlobal = true OR s = :storeId)
    """)
    Page<ProductBrand> findAvailableBrands(
            @Param("businessId") Long businessId,
            @Param("storeId") Long storeId,
            @Param("status") String status,
            Pageable pageable);

    Optional<ProductBrand> findByIdAndBusinessIdAndStatusNot(Long id, Long businessId, String status);

    boolean existsByBusinessIdAndBrandCodeIgnoreCaseAndStatusNot(Long businessId, String brandCode, String status);

    boolean existsByBusinessIdAndBrandCodeIgnoreCaseAndIdNotAndStatusNot(Long businessId, String brandCode, Long id, String status);
}
