package com.x.product.service;

import com.x.product.entity.Product;
import com.x.product.entity.ProductSaleChannel;
import com.x.product.entity.ProductVariant;
import com.x.product.repository.ProductRepository;
import com.x.redis.cache.CacheNames;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    /**
     * List cache — short TTL. Evicted on any product write.
     */
    @Cacheable(cacheNames = CacheNames.PRODUCTS, key = "'all'")
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * By-id cache — avoids repeated MySQL hits for the same product.
     */
    @Cacheable(cacheNames = CacheNames.PRODUCT_BY_ID, key = "#id")
    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = CacheNames.PRODUCTS, allEntries = true)
    })
    @Transactional
    public Product createProduct(Product product) {
        validateStoreOwnership(product);
        normalizeAndValidateCurrency(product);
        prepareVariants(product);
        validateSalesChannelPrices(product);
        Product saved = productRepository.save(product);
        return saved;
    }

    /**
     * Keeps the catalog model consistent: a product is never directly sold;
     * its variants are sold. Products without options receive one default
     * variant automatically.
     */
    private void prepareVariants(Product product) {
        List<ProductVariant> variants = product.getVariants();
        if (variants == null || variants.isEmpty()) {
            ProductVariant defaultVariant = ProductVariant.builder()
                    .variantName("Default")
                    .sku(product.getProductCode())
                    .barcode(product.getBarcode())
                    .costPrice(product.getCostPrice())
                    .posPrice(product.getSalePrice())
                    .status(product.getStatus())
                    .isDefault(true)
                    .build();
            variants = new ArrayList<>(List.of(defaultVariant));
            product.setVariants(variants);
        }

        long defaultVariantCount = variants.stream()
                .filter(variant -> Boolean.TRUE.equals(variant.getIsDefault()))
                .count();
        if (defaultVariantCount > 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A product can have only one default variant");
        }
        if (defaultVariantCount == 0 && variants.size() == 1) {
            variants.get(0).setIsDefault(true);
        }

        for (ProductVariant variant : variants) {
            variant.setProduct(product);
        }
    }

    private void validateSalesChannelPrices(Product product) {
        ProductSaleChannel salesChannel = product.getSalesChannel();
        if (salesChannel == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Product salesChannel is required: 1 (POS), 2 (ONLINE), or 3 (BOTH)");
        }

        for (ProductVariant variant : product.getVariants()) {
            boolean requiresPosPrice = salesChannel == ProductSaleChannel.POS || salesChannel == ProductSaleChannel.BOTH;
            boolean requiresOnlinePrice = salesChannel == ProductSaleChannel.ONLINE || salesChannel == ProductSaleChannel.BOTH;
            if (requiresPosPrice && variant.getPosPrice() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "POS price is required for each variant when salesChannel is POS or BOTH");
            }
            if (requiresOnlinePrice && variant.getOnlinePrice() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Online price is required for each variant when salesChannel is ONLINE or BOTH");
            }
        }
    }

    private void normalizeAndValidateCurrency(Product product) {
        String currencyCode = product.getCurrencyCode();
        if (currencyCode == null || currencyCode.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Product currencyCode is required");
        }

        String normalizedCurrencyCode = currencyCode.trim().toUpperCase(Locale.ROOT);
        try {
            Currency.getInstance(normalizedCurrencyCode);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Product currencyCode must be a valid ISO 4217 code");
        }
        product.setCurrencyCode(normalizedCurrencyCode);
    }

    private void validateStoreOwnership(Product product) {
        if (product.getStoreId() == null || product.getStoreId() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product storeId is required");
        }
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = CacheNames.PRODUCT_BY_ID, key = "#id"),
            @CacheEvict(cacheNames = CacheNames.PRODUCTS, allEntries = true)
    })
    @Transactional
    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        product.setProductName(productDetails.getProductName());
        if (productDetails.getCurrencyCode() != null) {
            normalizeAndValidateCurrency(productDetails);
            product.setCurrencyCode(productDetails.getCurrencyCode());
        }
        if (productDetails.getSalesChannel() != null) {
            product.setSalesChannel(productDetails.getSalesChannel());
        }
        product.setShortName(productDetails.getShortName());
        product.setBarcode(productDetails.getBarcode());
        product.setQrCode(productDetails.getQrCode());
        product.setCategory(productDetails.getCategory());
        product.setBrand(productDetails.getBrand());
        product.setUnit(productDetails.getUnit());
        product.setTax(productDetails.getTax());
        product.setThumbnail(productDetails.getThumbnail());
        product.setDescription(productDetails.getDescription());
        product.setCostPrice(productDetails.getCostPrice());
        product.setSalePrice(productDetails.getSalePrice());
        product.setWholesalePrice(productDetails.getWholesalePrice());
        product.setMinPrice(productDetails.getMinPrice());
        product.setWeight(productDetails.getWeight());
        product.setIsFeatured(productDetails.getIsFeatured());
        product.setIsSellable(productDetails.getIsSellable());
        product.setIsStockable(productDetails.getIsStockable());
        product.setStatus(productDetails.getStatus());
        return productRepository.save(product);
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = CacheNames.PRODUCT_BY_ID, key = "#id"),
            @CacheEvict(cacheNames = CacheNames.PRODUCTS, allEntries = true)
    })
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        productRepository.deleteById(id);
    }
}
