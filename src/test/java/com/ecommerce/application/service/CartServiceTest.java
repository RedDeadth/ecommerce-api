package com.ecommerce.application.service;

import com.ecommerce.application.dto.CartItemRequest;
import com.ecommerce.application.dto.CartItemResponse;
import com.ecommerce.domain.model.*;
import com.ecommerce.domain.repository.*;
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
class CartServiceTest {

    @Mock private CartItemRepository cartItemRepository;
    @Mock private ProductRepository productRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks
    private CartService cartService;

    private User testUser;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("user@test.com")
                .password("encoded")
                .fullName("Test User")
                .role(Role.USER)
                .build();

        testProduct = Product.builder()
                .id(1L)
                .name("Product")
                .description("Desc")
                .price(new BigDecimal("10.00"))
                .stock(50)
                .category("Cat")
                .active(true)
                .build();
    }

    @Test
    void addToCart_shouldCreateNewItem() {
        var request = new CartItemRequest(1L, 2);
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(testUser));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(cartItemRepository.findByUserAndProductId(testUser, 1L)).thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenAnswer(inv -> {
            CartItem ci = inv.getArgument(0);
            ci.setId(1L);
            return ci;
        });

        var response = cartService.addToCart("user@test.com", request);

        assertNotNull(response);
        assertEquals(2, response.quantity());
        assertEquals(new BigDecimal("20.00"), response.subtotal());
    }

    @Test
    void addToCart_shouldThrowWhenInsufficientStock() {
        var request = new CartItemRequest(1L, 999);
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(testUser));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        assertThrows(IllegalArgumentException.class,
                () -> cartService.addToCart("user@test.com", request));
    }

    @Test
    void getCart_shouldReturnUserItems() {
        var item = CartItem.builder()
                .id(1L)
                .user(testUser)
                .product(testProduct)
                .quantity(3)
                .build();
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(testUser));
        when(cartItemRepository.findByUser(testUser)).thenReturn(List.of(item));

        var cart = cartService.getCart("user@test.com");

        assertEquals(1, cart.size());
        assertEquals(3, cart.get(0).quantity());
    }
}
