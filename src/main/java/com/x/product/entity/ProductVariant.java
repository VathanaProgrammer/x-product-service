package com.x.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(name = "variant_name")
    private String variantName;

    @Column(nullable = false, unique = true)
    private String barcode;

    @Column(name = "is_default")
    private Boolean isDefault = false;

    @Column(columnDefinition = "TEXT")
    private String image;

    @Column(name = "cost_price")
    private BigDecimal costPrice;

    @Column(name = "pos_price")
    private BigDecimal posPrice;

    @Column(name = "compare_at_price")
    private BigDecimal compareAtPrice;

    @Column(name = "online_price")
    private BigDecimal onlinePrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @Column(name = "stock_alert_qty")
    private Integer stockAlertQty;

    private Integer status;

    @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductVariantAttributeValue> attributeValues;

    public String getDisplayName() {
        if (variantName != null && !variantName.isBlank()) {
            return variantName;
        }
        if (attributeValues != null && !attributeValues.isEmpty()) {
            return attributeValues.stream()
                    .map(va -> va.getAttributeValue() != null
                            ? va.getAttributeValue().getAttribute().getAttributeName() + ": "
                                    + va.getAttributeValue().getValue()
                            : "")
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.joining(" · "));
        }
        return null;
    }
}
