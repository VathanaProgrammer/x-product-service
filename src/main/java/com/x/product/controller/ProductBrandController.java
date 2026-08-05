package com.x.product.controller;

import com.sharedlib.response.ApiResponse;
import com.sharedlib.response.PageResponse;
import com.x.product.dto.ProductBrandRequest;
import com.x.product.dto.ProductBrandResponse;
import com.x.product.service.ProductBrandService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products/brands")
@RequiredArgsConstructor
@Validated
public class ProductBrandController {
    private final ProductBrandService brandService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ProductBrandResponse>>> list(
            @RequestParam @Positive Long businessId,
            @RequestParam(required = false) @Positive Long storeId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        var result = brandService.list(businessId, storeId, page, size);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), new PageResponse<>(
                result.getContent(), result.getNumber(), result.getSize(),
                result.getTotalElements(), result.getTotalPages(), result.hasNext())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductBrandResponse>> get(
            @PathVariable @Positive Long id,
            @RequestParam @Positive Long businessId) {
        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(), brandService.get(id, businessId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductBrandResponse>> create(
            @Valid @RequestBody ProductBrandRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        HttpStatus.CREATED.value(), "Product brand created",
                        brandService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductBrandResponse>> update(
            @PathVariable @Positive Long id,
            @RequestParam @Positive Long businessId,
            @Valid @RequestBody ProductBrandRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(), "Product brand updated",
                brandService.update(id, businessId, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable @Positive Long id,
            @RequestParam @Positive Long businessId) {
        brandService.softDelete(id, businessId);
        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(), "Product brand deleted", null));
    }
}
