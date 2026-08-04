package com.x.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record ProductCategoryRequest(
        @NotNull @Positive Long businessId,
        @NotEmpty Set<@NotNull @Positive Long> storeIds,
        @NotBlank @Size(max = 40)
        @Pattern(regexp = "[A-Za-z0-9_-]+", message = "must contain only letters, numbers, hyphens, and underscores")
        String categoryCode,
        @NotBlank @Size(max = 160) String categoryName,
        @Size(max = 2048) String image,
        @PositiveOrZero Integer sortOrder,
        CatalogStatus status) {
}
