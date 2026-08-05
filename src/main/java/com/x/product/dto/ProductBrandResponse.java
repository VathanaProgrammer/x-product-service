package com.x.product.dto;

import com.x.product.entity.ProductBrand;

import java.util.Set;

public record ProductBrandResponse(
        Long id,
        Long businessId,
        Set<Long> storeIds,
        String brandCode,
        String brandName,
        String logo,
        String status) {

    public static ProductBrandResponse from(ProductBrand brand) {
        Set<Long> stores = brand.getStoreIds() == null
                ? Set.of() : Set.copyOf(brand.getStoreIds());
        return new ProductBrandResponse(
                brand.getId(), brand.getBusinessId(), stores,
                brand.getBrandCode(), brand.getBrandName(),
                brand.getLogo(), brand.getStatus());
    }
}
