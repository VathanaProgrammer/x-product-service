package com.VyntraProductService.service;

import com.VyntraProductService.entity.Product;
import com.VyntraProductService.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = getProductById(id);
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

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
