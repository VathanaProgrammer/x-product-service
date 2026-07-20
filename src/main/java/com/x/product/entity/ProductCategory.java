package com.x.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "category_code")
    private String categoryCode;

    @Column(name = "category_name")
    private String categoryName;

    @Column(columnDefinition = "TEXT")
    private String image;

    @Column(name = "sort_order")
    private Integer sortOrder;

    private Integer status;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Product> products;
}
