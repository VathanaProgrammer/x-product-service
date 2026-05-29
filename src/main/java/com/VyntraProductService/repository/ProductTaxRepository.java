package com.VyntraProductService.repository;

import com.VyntraProductService.entity.ProductTax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTaxRepository extends JpaRepository<ProductTax, Long> {
}
