package com.x.product.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ProductSaleChannelConverter implements AttributeConverter<ProductSaleChannel, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ProductSaleChannel attribute) {
        return attribute == null ? null : attribute.getCode();
    }

    @Override
    public ProductSaleChannel convertToEntityAttribute(Integer databaseValue) {
        return databaseValue == null ? null : ProductSaleChannel.fromCode(databaseValue);
    }
}
