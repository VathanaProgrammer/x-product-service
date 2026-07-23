package com.x.product.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Product sale availability. The numeric values are part of the API contract.
 */
public enum ProductSaleChannel {
    POS(1),
    ONLINE(2),
    BOTH(3);

    private final int code;

    ProductSaleChannel(int code) {
        this.code = code;
    }

    @JsonValue
    public int getCode() {
        return code;
    }

    @JsonCreator
    public static ProductSaleChannel fromCode(int code) {
        for (ProductSaleChannel channel : values()) {
            if (channel.code == code) {
                return channel;
            }
        }
        throw new IllegalArgumentException("salesChannel must be 1 (POS), 2 (ONLINE), or 3 (BOTH)");
    }
}
