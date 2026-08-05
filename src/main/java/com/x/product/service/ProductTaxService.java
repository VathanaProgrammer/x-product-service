package com.x.product.service;

import com.x.product.dto.CatalogStatus;
import com.x.product.dto.ProductTaxRequest;
import com.x.product.dto.ProductTaxResponse;
import com.x.product.entity.ProductTax;
import com.x.product.repository.ProductTaxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductTaxService {
    private static final String DELETED = "DELETED";

    private final ProductTaxRepository taxRepository;

    @Transactional(readOnly = true)
    public Page<ProductTaxResponse> list(Long businessId, Long storeId, int page, int size) {
        PageRequest pageable = PageRequest.of(
                page, size, Sort.by("isDefault").descending().and(Sort.by("percentage").ascending()));
        Page<ProductTax> result = storeId == null
                ? taxRepository.findByBusinessIdAndStatusNot(businessId, DELETED, pageable)
                : taxRepository.findAvailableTaxes(businessId, storeId, DELETED, pageable);
        return result.map(ProductTaxResponse::from);
    }

    @Transactional(readOnly = true)
    public ProductTaxResponse get(Long id, Long businessId) {
        return ProductTaxResponse.from(require(id, businessId));
    }

    @Transactional
    public ProductTaxResponse create(ProductTaxRequest request) {
        String code = normalizeCode(request.taxCode());
        ensureCodeAvailable(request.businessId(), code, null);
        ProductTax tax = ProductTax.builder()
                .businessId(request.businessId())
                .build();
        applyRequest(tax, request, code);
        return ProductTaxResponse.from(taxRepository.save(tax));
    }

    @Transactional
    public ProductTaxResponse update(Long id, Long businessId, ProductTaxRequest request) {
        validateBusinessId(businessId, request.businessId());
        ProductTax tax = require(id, businessId);
        String code = normalizeCode(request.taxCode());
        ensureCodeAvailable(businessId, code, id);
        applyRequest(tax, request, code);
        return ProductTaxResponse.from(taxRepository.save(tax));
    }

    @Transactional
    public void softDelete(Long id, Long businessId) {
        ProductTax tax = require(id, businessId);
        tax.setStatus(DELETED);
        taxRepository.save(tax);
    }

    private ProductTax require(Long id, Long businessId) {
        return taxRepository.findByIdAndBusinessIdAndStatusNot(id, businessId, DELETED)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Product tax rate not found"));
    }

    private void ensureCodeAvailable(Long businessId, String code, Long excludedId) {
        boolean exists = excludedId == null
                ? taxRepository.existsByBusinessIdAndTaxCodeIgnoreCaseAndStatusNot(businessId, code, DELETED)
                : taxRepository.existsByBusinessIdAndTaxCodeIgnoreCaseAndIdNotAndStatusNot(businessId, code, excludedId, DELETED);
        if (exists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product tax code already exists");
        }
    }

    private void applyRequest(ProductTax tax, ProductTaxRequest request, String normalizedCode) {
        boolean isGlobal = request.isGlobal() == null || Boolean.TRUE.equals(request.isGlobal());
        tax.setTaxCode(normalizedCode);
        tax.setTaxName(request.taxName().trim());
        tax.setDescription(trim(request.description()));
        tax.setPercentage(request.percentage());
        tax.setIsDefault(Boolean.TRUE.equals(request.isDefault()));
        tax.setIsGlobal(isGlobal);
        tax.setStatus(status(request.status()));

        if (!isGlobal) {
            tax.setStoreIds(copyStoreIds(request.storeIds()));
        } else {
            tax.setStoreIds(request.storeIds() != null ? new LinkedHashSet<>(request.storeIds()) : new LinkedHashSet<>());
        }
    }

    private Set<Long> copyStoreIds(Set<Long> storeIds) {
        if (storeIds == null || storeIds.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one store ID is required for store-specific tax rates");
        }
        return new LinkedHashSet<>(storeIds);
    }

    private String normalizeCode(String value) {
        return value.trim().toUpperCase(Locale.ROOT);
    }

    private String status(CatalogStatus status) {
        return (status == null ? CatalogStatus.ACTIVE : status).name();
    }

    private String trim(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }

    private void validateBusinessId(Long expected, Long requested) {
        if (!expected.equals(requested)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "businessId cannot be changed");
        }
    }
}
