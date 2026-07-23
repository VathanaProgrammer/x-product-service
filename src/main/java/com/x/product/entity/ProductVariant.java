package com.x.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Product product;

    @Column(unique = true)
    private String sku;

    @Column(name = "variant_name")
    private String variantName;

    private String barcode;

    @Column(name = "is_default")
    private Boolean isDefault = false;

    @Column(columnDefinition = "TEXT")
    private String image;

    @Column(name = "cost_price")
    private BigDecimal costPrice;

    @Column(name = "pos_price")
    private BigDecimal posPrice;

    @Column(name = "online_price")
    private BigDecimal onlinePrice;

    @Column(name = "stock_alert_qty")
    private Integer stockAlertQty;

    private Integer status;

}
