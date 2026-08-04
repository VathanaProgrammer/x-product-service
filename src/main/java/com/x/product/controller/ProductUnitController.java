package com.x.product.controller;

import com.sharedlib.response.ApiResponse;
import com.sharedlib.response.PageResponse;
import com.x.product.dto.ProductUnitRequest;
import com.x.product.dto.ProductUnitResponse;
import com.x.product.service.ProductUnitService;
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
@RequestMapping("/api/v1/products/units")
@RequiredArgsConstructor
@Validated
public class ProductUnitController {
    private final ProductUnitService unitService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ProductUnitResponse>>> list(
            @RequestParam @Positive Long businessId,
            @RequestParam(required = false) @Positive Long storeId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        var result = unitService.list(businessId, storeId, page, size);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), new PageResponse<>(
                result.getContent(), result.getNumber(), result.getSize(),
                result.getTotalElements(), result.getTotalPages(), result.hasNext())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductUnitResponse>> get(
            @PathVariable @Positive Long id,
            @RequestParam @Positive Long businessId) {
        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(), unitService.get(id, businessId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductUnitResponse>> create(
            @Valid @RequestBody ProductUnitRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        HttpStatus.CREATED.value(), "Product unit created",
                        unitService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductUnitResponse>> update(
            @PathVariable @Positive Long id,
            @RequestParam @Positive Long businessId,
            @Valid @RequestBody ProductUnitRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(), "Product unit updated",
                unitService.update(id, businessId, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable @Positive Long id,
            @RequestParam @Positive Long businessId) {
        unitService.softDelete(id, businessId);
        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(), "Product unit deleted", null));
    }
}
