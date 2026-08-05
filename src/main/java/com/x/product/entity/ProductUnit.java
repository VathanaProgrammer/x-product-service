package com.x.product.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "units")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "business_id", nullable = false)
    private Long businessId;

    @ElementCollection
    @CollectionTable(name = "unit_stores", joinColumns = @JoinColumn(name = "unit_id"))
    @Column(name = "store_id")
    @Builder.Default
    private Set<Long> storeIds = new HashSet<>();

    @Column(name = "unit_code", nullable = false, length = 50)
    private String unitCode;

    @Column(name = "unit_name", nullable = false, length = 160)
    private String unitName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * true  = Shared across ALL stores in this business
     * false = Restricted to storeIds listed in unit_stores
     */
    @Column(name = "is_global", nullable = false)
    @Builder.Default
    private Boolean isGlobal = true;

    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private String status = "ACTIVE";

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
