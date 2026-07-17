package com.VyntraProductService.service;

import com.VyntraProductService.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    @Test
    void getProductByIdReturnsNotFoundWhenProductDoesNotExist() {
        ProductRepository repository = mock(ProductRepository.class);
        when(repository.findById(99L)).thenReturn(Optional.empty());
        ProductService service = new ProductService(repository);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.getProductById(99L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}
