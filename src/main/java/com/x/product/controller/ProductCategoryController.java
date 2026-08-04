package com.x.product.controller;

import com.sharedlib.response.ApiResponse;
import com.sharedlib.response.PageResponse;
import com.x.product.dto.ProductCategoryRequest;
import com.x.product.dto.ProductCategoryResponse;
import com.x.product.service.ProductCategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products/categories")
@RequiredArgsConstructor
@Validated
public class ProductCategoryController {
    private final ProductCategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ProductCategoryResponse>>> list(
            @RequestParam @Positive Long businessId,
            @RequestParam(required = false) @Positive Long storeId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        var result = categoryService.list(businessId, storeId, page, size);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), new PageResponse<>(
                result.getContent(), result.getNumber(), result.getSize(),
                result.getTotalElements(), result.getTotalPages(), result.hasNext())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductCategoryResponse>> get(
            @PathVariable @Positive Long id,
            @RequestParam @Positive Long businessId) {
        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(), categoryService.get(id, businessId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductCategoryResponse>> create(
            @Valid @RequestBody ProductCategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        HttpStatus.CREATED.value(), "Product category created",
                        categoryService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductCategoryResponse>> update(
            @PathVariable @Positive Long id,
            @RequestParam @Positive Long businessId,
            @Valid @RequestBody ProductCategoryRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(), "Product category updated",
                categoryService.update(id, businessId, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable @Positive Long id,
            @RequestParam @Positive Long businessId) {
        categoryService.softDelete(id, businessId);
        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(), "Product category deleted", null));
    }
}
