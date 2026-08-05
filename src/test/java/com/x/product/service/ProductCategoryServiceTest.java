package com.x.product.service;

import com.x.product.dto.CatalogStatus;
import com.x.product.dto.ProductCategoryRequest;
import com.x.product.dto.ProductCategoryResponse;
import com.x.product.entity.ProductCategory;
import com.x.product.repository.ProductCategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductCategoryServiceTest {

    @Test
    void createNormalizesValuesAndDefaultsOptionalFields() {
        ProductCategoryRepository repository = mock(ProductCategoryRepository.class);
        ProductCategoryRequest request = new ProductCategoryRequest(
                7L, Set.of(11L), "dry_goods", " Dry Goods ", "Dry goods category", null, null, false, true, null);
        when(repository.existsByBusinessIdAndCategoryCodeIgnoreCaseAndStatusNot(
                7L, "DRY_GOODS", "DELETED")).thenReturn(false);
        when(repository.save(any(ProductCategory.class))).thenAnswer(invocation -> {
            ProductCategory category = invocation.getArgument(0);
            category.setId(6L);
            return category;
        });

        ProductCategoryResponse result = new ProductCategoryService(repository).create(request);

        assertEquals(6L, result.id());
        assertEquals("DRY_GOODS", result.categoryCode());
        assertEquals("Dry Goods", result.categoryName());
        assertEquals("Dry goods category", result.description());
        assertEquals(Integer.valueOf(0), result.sortOrder());
        assertEquals(Boolean.FALSE, result.isFeatured());
        assertEquals(Boolean.TRUE, result.isGlobal());
        assertEquals("ACTIVE", result.status());
        assertNull(result.image());
    }

    @Test
    void createRejectsDuplicateActiveCode() {
        ProductCategoryRepository repository = mock(ProductCategoryRepository.class);
        ProductCategoryRequest request = new ProductCategoryRequest(
                7L, Set.of(11L), "DRY", "Dry", null, null, 1, false, true, CatalogStatus.ACTIVE);
        when(repository.existsByBusinessIdAndCategoryCodeIgnoreCaseAndStatusNot(
                7L, "DRY", "DELETED")).thenReturn(true);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> new ProductCategoryService(repository).create(request));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        verify(repository, never()).save(any());
    }

    @Test
    void getReturnsNotFoundForDeletedOrOtherBusinessCategory() {
        ProductCategoryRepository repository = mock(ProductCategoryRepository.class);
        when(repository.findByIdAndBusinessIdAndStatusNot(6L, 7L, "DELETED"))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> new ProductCategoryService(repository).get(6L, 7L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void listUsesStoreScopedQueryWhenStoreIdIsProvided() {
        ProductCategoryRepository repository = mock(ProductCategoryRepository.class);
        ProductCategory category = ProductCategory.builder()
                .id(6L)
                .businessId(7L)
                .storeIds(Set.of(11L))
                .categoryCode("DRY")
                .categoryName("Dry Goods")
                .sortOrder(1)
                .isFeatured(false)
                .isGlobal(true)
                .status("ACTIVE")
                .build();
        when(repository.findAvailableCategories(
                eq(7L), eq(11L), eq("DELETED"), any(Pageable.class)))
                .thenReturn(new PageImpl<>(java.util.List.of(category)));

        var result = new ProductCategoryService(repository).list(7L, 11L, 0, 20);

        assertEquals(1, result.getTotalElements());
        assertEquals("DRY", result.getContent().get(0).categoryCode());
    }

    @Test
    void deletePersistsDeletedStatus() {
        ProductCategoryRepository repository = mock(ProductCategoryRepository.class);
        ProductCategory category = ProductCategory.builder()
                .id(6L).businessId(7L).status("ACTIVE").build();
        when(repository.findByIdAndBusinessIdAndStatusNot(6L, 7L, "DELETED"))
                .thenReturn(Optional.of(category));

        new ProductCategoryService(repository).softDelete(6L, 7L);

        assertEquals("DELETED", category.getStatus());
        verify(repository).save(category);
    }
}
