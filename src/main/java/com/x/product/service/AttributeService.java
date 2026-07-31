package com.x.product.service;

import com.x.product.entity.ProductAttribute;
import com.x.product.entity.ProductAttributeValue;
import com.x.product.repository.ProductAttributeRepository;
import com.x.product.repository.ProductAttributeValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttributeService {
    private final ProductAttributeRepository attributeRepository;
    private final ProductAttributeValueRepository attributeValueRepository;

    @Transactional(readOnly = true)
    public List<ProductAttribute> getAttributesByBusiness(Long businessId) {
        return attributeRepository.findAllByBusinessId(businessId);
    }

    @Transactional(readOnly = true)
    public ProductAttribute getAttributeById(Long id) {
        return attributeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attribute not found"));
    }

    @Transactional
    public ProductAttribute createAttribute(ProductAttribute attribute) {
        if (attribute.getBusinessId() == null || attribute.getBusinessId() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attribute businessId is required");
        }
        if (attribute.getAttributeName() == null || attribute.getAttributeName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attribute name is required");
        }
        return attributeRepository.save(attribute);
    }

    @Transactional
    public void deleteAttribute(Long id) {
        ProductAttribute attribute = attributeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attribute not found"));
        attributeRepository.delete(attribute);
    }

    @Transactional(readOnly = true)
    public List<ProductAttributeValue> getAttributeValues(Long attributeId) {
        if (!attributeRepository.existsById(attributeId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attribute not found");
        }
        return attributeValueRepository.findAllByAttributeId(attributeId);
    }

    @Transactional
    public ProductAttributeValue addAttributeValue(Long attributeId, ProductAttributeValue value) {
        ProductAttribute attribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attribute not found"));
        value.setAttribute(attribute);
        return attributeValueRepository.save(value);
    }
}
