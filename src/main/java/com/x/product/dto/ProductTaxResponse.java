package com.x.product.dto;

import com.x.product.entity.ProductTax;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;

public record ProductTaxResponse(
        Long id,
        Long businessId,
        Set<Long> storeIds,
        String taxCode,
        String taxName,
        String description,
        BigDecimal percentage,
        Boolean isDefault,
        Boolean isGlobal,
        String status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt) {

    public static ProductTaxResponse from(ProductTax tax) {
        Set<Long> stores = tax.getStoreIds() == null
                ? Set.of() : Set.copyOf(tax.getStoreIds());
        return new ProductTaxResponse(
                tax.getId(),
                tax.getBusinessId(),
                stores,
                tax.getTaxCode(),
                tax.getTaxName(),
                tax.getDescription(),
                tax.getPercentage(),
                tax.getIsDefault(),
                tax.getIsGlobal(),
                tax.getStatus(),
                tax.getCreatedAt(),
                tax.getUpdatedAt());
    }
}
