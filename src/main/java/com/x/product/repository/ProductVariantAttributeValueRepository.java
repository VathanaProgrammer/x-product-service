package com.x.product.repository;

import com.x.product.entity.ProductVariantAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVariantAttributeValueRepository extends JpaRepository<ProductVariantAttributeValue, Long> {
    List<ProductVariantAttributeValue> findAllByVariantId(Long variantId);

    void deleteAllByVariantId(Long variantId);
}
