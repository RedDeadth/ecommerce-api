package com.ecommerce.application.service;

import com.ecommerce.application.dto.ProductRequest;
import com.ecommerce.application.dto.ProductResponse;
import com.ecommerce.domain.model.Product;
import com.ecommerce.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .description("Description")
                .price(new BigDecimal("29.99"))
                .stock(50)
                .imageUrl("http://img.jpg")
                .category("Electronics")
                .active(true)
                .build();
    }

    @Test
    void create_shouldSaveAndReturnProduct() {
        var request = new ProductRequest("New Product", "Desc", new BigDecimal("19.99"), 10, null, "Home");

        when(productRepository.save(any(Product.class))).thenAnswer(inv -> {
            Product p = inv.getArgument(0);
            p.setId(2L);
            return p;
        });

        var response = productService.create(request);

        assertNotNull(response);
        assertEquals("New Product", response.name());
        assertEquals(new BigDecimal("19.99"), response.price());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void getById_shouldReturnProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        var response = productService.getById(1L);
        assertEquals("Test Product", response.name());
    }

    @Test
    void getById_shouldThrowWhenNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> productService.getById(99L));
    }

    @Test
    void delete_shouldSoftDelete() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);
        productService.delete(1L);
        assertFalse(sampleProduct.getActive());
        verify(productRepository).save(sampleProduct);
    }

    @Test
    void getAllActive_shouldReturnOnlyActiveProducts() {
        when(productRepository.findByActiveTrue()).thenReturn(List.of(sampleProduct));
        var products = productService.getAllActive();
        assertEquals(1, products.size());
        verify(productRepository).findByActiveTrue();
    }
}
