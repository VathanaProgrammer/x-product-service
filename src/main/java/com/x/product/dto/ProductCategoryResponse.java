package com.x.product.dto;

import com.x.product.entity.ProductCategory;

import java.util.Set;

public record ProductCategoryResponse(
        Long id,
        Long businessId,
        Set<Long> storeIds,
        String categoryCode,
        String categoryName,
        String image,
        Integer sortOrder,
        String status) {

    public static ProductCategoryResponse from(ProductCategory category) {
        Set<Long> stores = category.getStoreIds() == null
                ? Set.of() : Set.copyOf(category.getStoreIds());
        return new ProductCategoryResponse(
                category.getId(), category.getBusinessId(), stores,
                category.getCategoryCode(), category.getCategoryName(),
                category.getImage(), category.getSortOrder(), category.getStatus());
    }
}
