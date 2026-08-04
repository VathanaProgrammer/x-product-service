package com.x.product.dto;

import com.x.product.entity.ProductUnit;

import java.util.Set;

public record ProductUnitResponse(
        Long id,
        Long businessId,
        Set<Long> storeIds,
        String unitCode,
        String unitName,
        String status) {

    public static ProductUnitResponse from(ProductUnit unit) {
        Set<Long> stores = unit.getStoreIds() == null
                ? Set.of() : Set.copyOf(unit.getStoreIds());
        return new ProductUnitResponse(
                unit.getId(), unit.getBusinessId(), stores,
                unit.getUnitCode(), unit.getUnitName(), unit.getStatus());
    }
}
