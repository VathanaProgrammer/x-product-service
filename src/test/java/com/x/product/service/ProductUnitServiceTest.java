package com.x.product.service;

import com.x.product.dto.CatalogStatus;
import com.x.product.dto.ProductUnitRequest;
import com.x.product.dto.ProductUnitResponse;
import com.x.product.entity.ProductUnit;
import com.x.product.repository.ProductUnitRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductUnitServiceTest {

    @Test
    void createNormalizesValuesAndDefaultsStatus() {
        ProductUnitRepository repository = mock(ProductUnitRepository.class);
        ProductUnitRequest request = new ProductUnitRequest(
                7L, Set.of(11L, 12L), "box_unit", " Box ", "Standard box unit", true, null);
        when(repository.existsByBusinessIdAndUnitCodeIgnoreCaseAndStatusNot(
                7L, "BOX_UNIT", "DELETED")).thenReturn(false);
        when(repository.save(any(ProductUnit.class))).thenAnswer(invocation -> {
            ProductUnit unit = invocation.getArgument(0);
            unit.setId(5L);
            return unit;
        });

        ProductUnitResponse result = new ProductUnitService(repository).create(request);

        assertEquals(5L, result.id());
        assertEquals("BOX_UNIT", result.unitCode());
        assertEquals("Box", result.unitName());
        assertEquals("Standard box unit", result.description());
        assertEquals("ACTIVE", result.status());
    }

    @Test
    void createRejectsDuplicateActiveCode() {
        ProductUnitRepository repository = mock(ProductUnitRepository.class);
        ProductUnitRequest request = new ProductUnitRequest(
                7L, Set.of(11L), "BOX", "Box", null, true, CatalogStatus.ACTIVE);
        when(repository.existsByBusinessIdAndUnitCodeIgnoreCaseAndStatusNot(
                7L, "BOX", "DELETED")).thenReturn(true);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> new ProductUnitService(repository).create(request));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        verify(repository, never()).save(any());
    }

    @Test
    void updateRejectsBusinessIdChange() {
        ProductUnitRepository repository = mock(ProductUnitRepository.class);
        ProductUnitRequest request = new ProductUnitRequest(
                8L, Set.of(11L), "BOX", "Box", null, true, CatalogStatus.ACTIVE);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> new ProductUnitService(repository).update(5L, 7L, request));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verify(repository, never()).findByIdAndBusinessIdAndStatusNot(any(), any(), any());
    }

    @Test
    void listUsesStoreScopedQueryWhenStoreIdIsProvided() {
        ProductUnitRepository repository = mock(ProductUnitRepository.class);
        ProductUnit unit = ProductUnit.builder()
                .id(5L)
                .businessId(7L)
                .storeIds(Set.of(11L))
                .unitCode("BOX")
                .unitName("Box")
                .isGlobal(true)
                .status("ACTIVE")
                .build();
        when(repository.findAvailableUnits(
                eq(7L), eq(11L), eq("DELETED"), any(Pageable.class)))
                .thenReturn(new PageImpl<>(java.util.List.of(unit)));

        var result = new ProductUnitService(repository).list(7L, 11L, 0, 20);

        assertEquals(1, result.getTotalElements());
        assertEquals("BOX", result.getContent().get(0).unitCode());
    }

    @Test
    void deletePersistsDeletedStatus() {
        ProductUnitRepository repository = mock(ProductUnitRepository.class);
        ProductUnit unit = ProductUnit.builder().id(5L).businessId(7L).status("ACTIVE").build();
        when(repository.findByIdAndBusinessIdAndStatusNot(5L, 7L, "DELETED"))
                .thenReturn(Optional.of(unit));

        new ProductUnitService(repository).softDelete(5L, 7L);

        assertEquals("DELETED", unit.getStatus());
        verify(repository).save(categoryOrUnit(unit));
    }

    private ProductUnit categoryOrUnit(ProductUnit unit) {
        return unit;
    }
}
