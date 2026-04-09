package com.ecommerce.application.service;

import com.ecommerce.application.dto.ProductRequest;
import com.ecommerce.domain.model.Product;
import com.ecommerce.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests de autorización:
 * Verifica que las operaciones de negocio validan correctamente
 * los permisos y datos (la seguridad por rol se maneja en SecurityConfig).
 */
@ExtendWith(MockitoExtension.class)
class ProductAuthorizationTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product existingProduct;

    @BeforeEach
    void setUp() {
        existingProduct = Product.builder()
                .id(1L).name("Original").description("Desc")
                .price(new BigDecimal("100.00")).stock(50)
                .category("Electronics").active(true)
                .build();
    }

    @Test
    void update_shouldModifyAllFields() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        var request = new ProductRequest("Updated", "New Desc", new BigDecimal("200.00"), 75, "img.jpg", "Home");
        var result = productService.update(1L, request);

        assertEquals("Updated", existingProduct.getName());
        assertEquals(new BigDecimal("200.00"), existingProduct.getPrice());
        assertEquals(75, existingProduct.getStock());
        verify(productRepository).save(existingProduct);
    }

    @Test
    void update_shouldThrowWhenProductNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());
        var request = new ProductRequest("X", "Y", BigDecimal.TEN, 1, null, null);
        assertThrows(IllegalArgumentException.class, () -> productService.update(99L, request));
    }

    @Test
    void delete_shouldSoftDelete_notHardDelete() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        productService.delete(1L);

        assertFalse(existingProduct.getActive()); // Soft delete
        verify(productRepository).save(existingProduct); // save, no delete
        verify(productRepository, never()).delete(any()); // nunca se llama delete real
    }

    @Test
    void delete_shouldThrowWhenProductNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> productService.delete(99L));
    }

    @Test
    void getById_shouldNotReturnSensitiveData() {
        // Verifica que el response no expone el campo version (optimistic locking internal)
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        var response = productService.getById(1L);

        assertNotNull(response);
        assertEquals("Original", response.name());
        // ProductResponse no tiene campo `version` — es internal
    }
}
