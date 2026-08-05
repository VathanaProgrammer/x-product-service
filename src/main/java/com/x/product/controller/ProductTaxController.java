package com.x.product.controller;

import com.sharedlib.response.ApiResponse;
import com.sharedlib.response.PageResponse;
import com.x.product.dto.ProductTaxRequest;
import com.x.product.dto.ProductTaxResponse;
import com.x.product.service.ProductTaxService;
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
@RequestMapping("/api/v1/products/taxes")
@RequiredArgsConstructor
@Validated
public class ProductTaxController {
    private final ProductTaxService taxService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ProductTaxResponse>>> list(
            @RequestParam @Positive Long businessId,
            @RequestParam(required = false) @Positive Long storeId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        var result = taxService.list(businessId, storeId, page, size);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), new PageResponse<>(
                result.getContent(), result.getNumber(), result.getSize(),
                result.getTotalElements(), result.getTotalPages(), result.hasNext())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductTaxResponse>> get(
            @PathVariable @Positive Long id,
            @RequestParam @Positive Long businessId) {
        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(), taxService.get(id, businessId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductTaxResponse>> create(
            @Valid @RequestBody ProductTaxRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        HttpStatus.CREATED.value(), "Product tax created",
                        taxService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductTaxResponse>> update(
            @PathVariable @Positive Long id,
            @RequestParam @Positive Long businessId,
            @Valid @RequestBody ProductTaxRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(), "Product tax updated",
                taxService.update(id, businessId, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable @Positive Long id,
            @RequestParam @Positive Long businessId) {
        taxService.softDelete(id, businessId);
        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(), "Product tax deleted", null));
    }
}
