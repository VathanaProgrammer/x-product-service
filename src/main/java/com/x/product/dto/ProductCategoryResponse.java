package com.x.product.dto;

import com.x.product.entity.ProductCategory;

import java.time.OffsetDateTime;
import java.util.Set;

public record ProductCategoryResponse(
        Long id,
        Long businessId,
        Set<Long> storeIds,
        String categoryCode,
        String categoryName,
        String description,
        String image,
        Integer sortOrder,
        Boolean isFeatured,
        Boolean isGlobal,
        String status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt) {

    public static ProductCategoryResponse from(ProductCategory category) {
        Set<Long> stores = category.getStoreIds() == null
                ? Set.of() : Set.copyOf(category.getStoreIds());
        return new ProductCategoryResponse(
                category.getId(),
                category.getBusinessId(),
                stores,
                category.getCategoryCode(),
                category.getCategoryName(),
                category.getDescription(),
                category.getImage(),
                category.getSortOrder(),
                category.getIsFeatured(),
                category.getIsGlobal(),
                category.getStatus(),
                category.getCreatedAt(),
                category.getUpdatedAt());
    }
}
