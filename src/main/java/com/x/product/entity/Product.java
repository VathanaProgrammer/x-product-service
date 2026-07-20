package com.x.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "business_id")
    private Long businessId;

    @Column(name = "product_code", unique = true)
    private String productCode;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "short_name")
    private String shortName;

    private String barcode;

    @Column(name = "qr_code")
    private String qrCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ProductCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private ProductBrand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id")
    private ProductUnit unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_id")
    private ProductTax tax;

    @Column(columnDefinition = "TEXT")
    private String thumbnail;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "cost_price")
    private BigDecimal costPrice;

    @Column(name = "sale_price")
    private BigDecimal salePrice;

    @Column(name = "wholesale_price")
    private BigDecimal wholesalePrice;

    @Column(name = "min_price")
    private BigDecimal minPrice;

    private BigDecimal weight;

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @Column(name = "is_sellable")
    private Boolean isSellable = true;

    @Column(name = "is_stockable")
    private Boolean isStockable = true;

    private Integer status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductVariant> variants;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductImage> images;
}
