package com.x.product.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "brand_code")
    private String brandCode;

    @Column(name = "brand_name")
    private String brandName;

    @Column(columnDefinition = "TEXT")
    private String logo;

    private Integer status;
}
