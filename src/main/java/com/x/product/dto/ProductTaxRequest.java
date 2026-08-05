package com.x.product.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.Set;

public record ProductTaxRequest(
        @NotNull @Positive Long businessId,
        Set<Long> storeIds,
        @NotBlank @Size(max = 50)
        @Pattern(regexp = "[A-Za-z0-9_-]+", message = "must contain only letters, numbers, hyphens, and underscores")
        String taxCode,
        @NotBlank @Size(max = 100) String taxName,
        String description,
        @NotNull @DecimalMin("0.00") @DecimalMax("100.00") BigDecimal percentage,
        Boolean isDefault,
        Boolean isGlobal,
        CatalogStatus status) {
}
