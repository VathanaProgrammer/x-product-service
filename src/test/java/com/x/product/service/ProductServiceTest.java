package com.x.product.service;

import com.x.product.entity.Product;
import com.x.product.entity.ProductSaleChannel;
import com.x.product.entity.ProductVariant;
import com.x.product.repository.ProductRepository;
import com.x.product.repository.ProductVariantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    @Test
    void getProductByIdReturnsNotFoundWhenProductDoesNotExist() {
        ProductRepository repository = mock(ProductRepository.class);
        when(repository.findById(99L)).thenReturn(Optional.empty());
        ProductService service = new ProductService(repository, mock(ProductVariantRepository.class));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.getProductById(99L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void createProductRejectsMissingVariant() {
        ProductRepository repository = mock(ProductRepository.class);
        Product product = Product.builder()
                .storeId(1L)
                .productCode("NOTEBOOK-001")
                .productName("Notebook")
                .currencyCode("khr")
                .salesChannel(ProductSaleChannel.POS)
                .build();
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> new ProductService(repository, mock(ProductVariantRepository.class)).createProduct(product));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void createProductConnectsProvidedVariantToProduct() {
        ProductRepository repository = mock(ProductRepository.class);
        ProductVariant variant = ProductVariant.builder()
                .sku("SHIRT-BLUE-M")
                .barcode("1234567890123")
                .posPrice(new BigDecimal("15.00"))
                .onlinePrice(new BigDecimal("17.00"))
                .build();
        Product product = Product.builder()
                .storeId(1L)
                .productCode("SHIRT")
                .currencyCode("USD")
                .salesChannel(ProductSaleChannel.BOTH)
                .variants(java.util.List.of(variant))
                .build();
        when(repository.save(product)).thenReturn(product);

        new ProductService(repository, mock(ProductVariantRepository.class)).createProduct(product);

        assertEquals(true, variant.getIsDefault());
        assertEquals(product, variant.getProduct());
    }

    @Test
    void createProductRejectsMissingCurrencyCode() {
        ProductRepository repository = mock(ProductRepository.class);
        Product product = Product.builder()
                .storeId(1L)
                .productCode("NOTEBOOK-001")
                .salesChannel(ProductSaleChannel.POS)
                .build();

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> new ProductService(repository, mock(ProductVariantRepository.class)).createProduct(product));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void createProductRejectsMissingStoreId() {
        ProductRepository repository = mock(ProductRepository.class);
        Product product = Product.builder()
                .productCode("NOTEBOOK-001")
                .currencyCode("KHR")
                .salesChannel(ProductSaleChannel.POS)
                .variants(java.util.List.of(ProductVariant.builder()
                        .sku("NOTEBOOK-001").barcode("100001")
                        .posPrice(new BigDecimal("2.50")).build()))
                .build();

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> new ProductService(repository, mock(ProductVariantRepository.class)).createProduct(product));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }
}
