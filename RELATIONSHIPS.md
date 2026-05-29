# Vyntra Product Service - Entity Relationship Diagram

This diagram illustrates the relationships between the entities within the Vyntra Product Service.

```mermaid
erDiagram
    Product ||--o{ ProductVariant : "has many"
    Product ||--o{ ProductImage : "has many"
    Product }o--|| ProductCategory : "belongs to"
    Product }o--|| ProductBrand : "belongs to"
    Product }o--|| ProductUnit : "uses"
    Product }o--|| ProductTax : "applies"

    ProductCategory ||--o{ ProductCategory : "parent/child"

    ProductVariant ||--o{ ProductVariantAttributeValue : "has many"
    ProductVariantAttributeValue }o--|| ProductAttributeValue : "references"
    ProductAttributeValue }o--|| ProductAttribute : "belongs to"

    Product {
        Long id PK
        Long businessId
        String productCode
        String productName
        String shortName
        String barcode
        String qrCode
        String thumbnail
        String description
        BigDecimal costPrice
        BigDecimal salePrice
        BigDecimal wholesalePrice
        BigDecimal minPrice
        BigDecimal weight
        Boolean isFeatured
        Boolean isSellable
        Boolean isStockable
        Integer status
        LocalDateTime createdAt
        LocalDateTime updatedAt
    }

    ProductVariant {
        Long id PK
        Long productId FK
        String sku
        String image
        BigDecimal costPrice
        BigDecimal salePrice
        Integer stockAlertQty
        Integer status
    }

    ProductImage {
        Long id PK
        Long productId FK
        String imageUrl
        Boolean isVisible
    }

    ProductCategory {
        Long id PK
        Long businessId
        Long parentId
        String categoryCode
        String categoryName
        String image
        Integer sortOrder
        Integer status
    }

    ProductBrand {
        Long id PK
        Long businessId
        String brandCode
        String brandName
        String logo
        Integer status
    }

    ProductUnit {
        Long id PK
        Long businessId
        String unitCode
        String unitName
    }

    ProductTax {
        Long id PK
        Long businessId
        String taxCode
        String taxName
        BigDecimal percentage
    }

    ProductAttribute {
        Long id PK
        Long businessId
        String attributeName
    }

    ProductAttributeValue {
        Long id PK
        Long attributeId FK
        String value
    }

    ProductVariantAttributeValue {
        Long id PK
        Long variantId FK
        Long attributeValueId FK
    }
```

## Relationship Summary
1.  **Product** is the central entity.
2.  A **Product** can have multiple **ProductVariants** (e.g., different sizes or colors).
3.  A **Product** can have multiple **ProductImages**.
4.  **Product** links to **ProductCategory**, **ProductBrand**, **ProductUnit**, and **ProductTax**.
5.  **ProductCategory** supports a hierarchical structure via `parentId`.
6.  **ProductVariants** are linked to specific **ProductAttributeValues** (like "Red", "Large") through the **ProductVariantAttributeValue** join table.
7.  **ProductAttributeValues** belong to a **ProductAttribute** (like "Color", "Size").
