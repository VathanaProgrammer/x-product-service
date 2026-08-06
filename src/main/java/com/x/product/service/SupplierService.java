package com.x.product.service;

import com.x.product.entity.Supplier;
import com.x.product.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private static final String DELETED = "DELETED";
    private final SupplierRepository supplierRepository;

    @Transactional(readOnly = true)
    public Page<Supplier> list(Long businessId, Long storeId, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("supplierName").ascending());
        return storeId == null
                ? supplierRepository.findByBusinessIdAndStatusNot(businessId, DELETED, pageable)
                : supplierRepository.findAvailableSuppliers(businessId, storeId, DELETED, pageable);
    }

    @Transactional(readOnly = true)
    public Supplier get(Long id, Long businessId) {
        return supplierRepository.findByIdAndBusinessIdAndStatusNot(id, businessId, DELETED)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplier not found"));
    }

    @Transactional
    public Supplier create(Supplier supplier) {
        if (supplier.getStatus() == null) {
            supplier.setStatus("ACTIVE");
        }
        return supplierRepository.save(supplier);
    }
}
