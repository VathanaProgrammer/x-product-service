package com.x.product.repository;

import com.x.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByStoreIdAndProductCode(Long storeId, String productCode);

    Page<Product> findAllByStoreId(Long storeId, Pageable pageable);
}
