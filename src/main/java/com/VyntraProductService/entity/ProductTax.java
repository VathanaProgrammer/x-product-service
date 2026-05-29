package com.VyntraProductService.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "taxes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductTax {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "business_id")
    private Long businessId;

    @Column(name = "tax_code")
    private String taxCode;

    @Column(name = "tax_name")
    private String taxName;

    private BigDecimal percentage;
}
