package com.x.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

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

    @ElementCollection
    @CollectionTable(name = "tax_stores", joinColumns = @JoinColumn(name = "tax_id"))
    @Column(name = "store_id")
    @Builder.Default
    private Set<Long> storeIds = new HashSet<>();

    @Column(name = "tax_code")
    private String taxCode;

    @Column(name = "tax_name")
    private String taxName;

    private BigDecimal percentage;

    private String status;
}
