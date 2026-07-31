package com.x.product.controller;

import com.x.product.entity.ProductAttribute;
import com.x.product.entity.ProductAttributeValue;
import com.x.product.service.AttributeService;
import com.sharedlib.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products/attributes")
@RequiredArgsConstructor
public class AttributeController {
    private final AttributeService attributeService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAttributes(
            @RequestParam @jakarta.validation.constraints.Positive Long businessId) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(),
                attributeService.getAttributesByBusiness(businessId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductAttribute>> getAttribute(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(),
                attributeService.getAttributeById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductAttribute>> createAttribute(@RequestBody ProductAttribute attribute) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Attribute created",
                        attributeService.createAttribute(attribute)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAttribute(@PathVariable Long id) {
        attributeService.deleteAttribute(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Attribute deleted", null));
    }

    @GetMapping("/{attributeId}/values")
    public ResponseEntity<ApiResponse<?>> getAttributeValues(@PathVariable Long attributeId) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(),
                attributeService.getAttributeValues(attributeId)));
    }

    @PostMapping("/{attributeId}/values")
    public ResponseEntity<ApiResponse<ProductAttributeValue>> addAttributeValue(
            @PathVariable Long attributeId,
            @RequestBody ProductAttributeValue value) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Attribute value added",
                        attributeService.addAttributeValue(attributeId, value)));
    }
}
