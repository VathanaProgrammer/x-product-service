package com.VyntraProductService.service;

import com.VyntraProductService.entity.Product;
import com.VyntraProductService.repository.ProductRepository;
import com.vyntra.redis.cache.CacheNames;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
        Product saved = productRepository.save(product);
        return saved;
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
