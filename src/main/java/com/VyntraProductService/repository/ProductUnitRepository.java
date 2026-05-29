package com.VyntraProductService.repository;

import com.VyntraProductService.entity.ProductUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductUnitRepository extends JpaRepository<ProductUnit, Long> {
}
