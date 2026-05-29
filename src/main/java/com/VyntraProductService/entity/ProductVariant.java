package com.VyntraProductService.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "product_variants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(unique = true)
    private String sku;

    @Column(columnDefinition = "TEXT")
    private String image;

    @Column(name = "cost_price")
    private BigDecimal costPrice;

    @Column(name = "sale_price")
    private BigDecimal salePrice;

    @Column(name = "stock_alert_qty")
    private Integer stockAlertQty;

    private Integer status;
}
