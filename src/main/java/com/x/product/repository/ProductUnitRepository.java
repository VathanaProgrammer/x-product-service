package com.x.product.repository;

import com.x.product.entity.ProductUnit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductUnitRepository extends JpaRepository<ProductUnit, Long> {
    Page<ProductUnit> findByBusinessIdAndStatusNot(
            Long businessId, String status, Pageable pageable);

    @Query("""
        SELECT DISTINCT u FROM ProductUnit u
        LEFT JOIN u.storeIds s
        WHERE u.businessId = :businessId
          AND u.status != :status
          AND (u.isGlobal = true OR s = :storeId)
    """)
    Page<ProductUnit> findAvailableUnits(
            @Param("businessId") Long businessId,
            @Param("storeId") Long storeId,
            @Param("status") String status,
            Pageable pageable);

    Optional<ProductUnit> findByIdAndBusinessIdAndStatusNot(
            Long id, Long businessId, String status);

    boolean existsByBusinessIdAndUnitCodeIgnoreCaseAndStatusNot(
            Long businessId, String unitCode, String status);

    boolean existsByBusinessIdAndUnitCodeIgnoreCaseAndIdNotAndStatusNot(
            Long businessId, String unitCode, Long id, String status);
}
