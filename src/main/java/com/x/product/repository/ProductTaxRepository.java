package com.x.product.repository;

import com.x.product.entity.ProductTax;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductTaxRepository extends JpaRepository<ProductTax, Long> {

    Page<ProductTax> findByBusinessIdAndStatusNot(Long businessId, String status, Pageable pageable);

    @Query("""
        SELECT DISTINCT t FROM ProductTax t
        LEFT JOIN t.storeIds s
        WHERE t.businessId = :businessId
          AND t.status != :status
          AND (t.isGlobal = true OR s = :storeId)
    """)
    Page<ProductTax> findAvailableTaxes(
            @Param("businessId") Long businessId,
            @Param("storeId") Long storeId,
            @Param("status") String status,
            Pageable pageable);

    Optional<ProductTax> findByIdAndBusinessIdAndStatusNot(Long id, Long businessId, String status);

    boolean existsByBusinessIdAndTaxCodeIgnoreCaseAndStatusNot(Long businessId, String taxCode, String status);

    boolean existsByBusinessIdAndTaxCodeIgnoreCaseAndIdNotAndStatusNot(Long businessId, String taxCode, Long id, String status);
}
