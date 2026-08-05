package com.x.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record ProductBrandRequest(
        @NotNull @Positive Long businessId,
        Set<Long> storeIds,
        @NotBlank @Size(max = 50) String brandCode,
        @NotBlank @Size(max = 100) String brandName,
        String description,
        String logo,
        Boolean isFeatured,
        Boolean isGlobal,
        CatalogStatus status) {
}
