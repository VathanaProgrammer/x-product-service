package com.VyntraProductService.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_attribute_values")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAttributeValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_id")
    private ProductAttribute attribute;

    private String value;
}
