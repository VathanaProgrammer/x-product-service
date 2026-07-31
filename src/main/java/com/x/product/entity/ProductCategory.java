package com.x.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "business_id")
    private Long businessId;

    @ElementCollection
    @CollectionTable(name = "category_stores", joinColumns = @JoinColumn(name = "category_id"))
    @Column(name = "store_id")
    @Builder.Default
    private Set<Long> storeIds = new HashSet<>();

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_code")
    private String categoryCode;

    @Column(name = "category_name")
    private String categoryName;

    @Column(columnDefinition = "TEXT")
    private String image;

    @Column(name = "sort_order")
    private Integer sortOrder;

    private String status;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Product> products;
}
