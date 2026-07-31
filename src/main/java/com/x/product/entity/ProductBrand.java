package com.x.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "brands")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductBrand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "business_id")
    private Long businessId;

    @ElementCollection
    @CollectionTable(name = "brand_stores", joinColumns = @JoinColumn(name = "brand_id"))
    @Column(name = "store_id")
    @Builder.Default
    private Set<Long> storeIds = new HashSet<>();

    @Column(name = "brand_code")
    private String brandCode;

    @Column(name = "brand_name")
    private String brandName;

    @Column(columnDefinition = "TEXT")
    private String logo;

    private String status;
}
