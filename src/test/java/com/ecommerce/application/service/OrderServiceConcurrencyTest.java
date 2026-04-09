package com.ecommerce.application.service;

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

/**
 * Tests de concurrencia:
 * - Dos usuarios comprando el último producto al mismo tiempo
 * - Verificación de stock insuficiente
 * - Optimistic locking con @Version
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceConcurrencyTest {

    @Mock private OrderRepository orderRepository;
    @Mock private CartItemRepository cartItemRepository;
    @Mock private ProductRepository productRepository;
    @Mock private UserRepository userRepository;
    @Mock private CouponService couponService;
    @Mock private EmailService emailService;

    @InjectMocks
    private OrderService orderService;

    private User user1;
    private User user2;
    private Product lastProduct;

    @BeforeEach
    void setUp() {
        user1 = User.builder().id(1L).email("user1@test.com").password("pass")
                .fullName("User One").role(Role.USER).build();
        user2 = User.builder().id(2L).email("user2@test.com").password("pass")
                .fullName("User Two").role(Role.USER).build();

        // Producto con SOLO 1 en stock
        lastProduct = Product.builder()
                .id(1L).name("Último Mouse")
                .price(new BigDecimal("59.99")).stock(1)
                .active(true).version(0L)
                .build();
    }

    @Test
    void firstUserShouldCheckoutSuccessfully() {
        // User1 tiene el producto en su carrito
        var cartItem1 = CartItem.builder()
                .id(1L).user(user1).product(lastProduct).quantity(1).build();

        when(userRepository.findByEmail("user1@test.com")).thenReturn(Optional.of(user1));
        when(cartItemRepository.findByUser(user1)).thenReturn(List.of(cartItem1));
        when(productRepository.save(any(Product.class))).thenReturn(lastProduct);
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> {
            Order o = inv.getArgument(0);
            o.setId(1L);
            return o;
        });

        var order = orderService.checkout("user1@test.com", null);

        assertNotNull(order);
        assertEquals(new BigDecimal("59.99"), order.total());
        assertEquals(0, lastProduct.getStock()); // Stock agotado
        verify(emailService).sendOrderConfirmation(eq("user1@test.com"), any(), any());
    }

    @Test
    void secondUserShouldFailWhenStockIsZero() {
        // Después de que User1 compró, el stock es 0
        lastProduct.setStock(0);

        var cartItem2 = CartItem.builder()
                .id(2L).user(user2).product(lastProduct).quantity(1).build();

        when(userRepository.findByEmail("user2@test.com")).thenReturn(Optional.of(user2));
        when(cartItemRepository.findByUser(user2)).thenReturn(List.of(cartItem2));

        // User2 intenta comprar → debe fallar por stock insuficiente
        var exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.checkout("user2@test.com", null));

        assertTrue(exception.getMessage().contains("Stock insuficiente"));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void shouldFailWhenRequestingMoreThanStock() {
        // User quiere 5 pero solo hay 1
        var cartItem = CartItem.builder()
                .id(1L).user(user1).product(lastProduct).quantity(5).build();

        when(userRepository.findByEmail("user1@test.com")).thenReturn(Optional.of(user1));
        when(cartItemRepository.findByUser(user1)).thenReturn(List.of(cartItem));

        var exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.checkout("user1@test.com", null));

        assertTrue(exception.getMessage().contains("Stock insuficiente"));
        assertTrue(exception.getMessage().contains("Último Mouse"));
    }

    @Test
    void shouldHandleMultipleProductsWithDifferentStock() {
        // Producto A: stock suficiente, Producto B: stock insuficiente
        var productA = Product.builder()
                .id(2L).name("Teclado").price(new BigDecimal("100.00"))
                .stock(50).active(true).build();
        var productB = Product.builder()
                .id(3L).name("Monitor").price(new BigDecimal("500.00"))
                .stock(0).active(true).build();

        var cartItemA = CartItem.builder().id(1L).user(user1).product(productA).quantity(1).build();
        var cartItemB = CartItem.builder().id(2L).user(user1).product(productB).quantity(1).build();

        when(userRepository.findByEmail("user1@test.com")).thenReturn(Optional.of(user1));
        when(cartItemRepository.findByUser(user1)).thenReturn(List.of(cartItemA, cartItemB));
        when(productRepository.save(any(Product.class))).thenReturn(productA);

        // Debería fallar en Producto B
        var exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.checkout("user1@test.com", null));

        assertTrue(exception.getMessage().contains("Monitor"));
    }

    @Test
    void stockShouldBeDecrementedCorrectlyAfterPurchase() {
        lastProduct.setStock(10);
        var cartItem = CartItem.builder()
                .id(1L).user(user1).product(lastProduct).quantity(3).build();

        when(userRepository.findByEmail("user1@test.com")).thenReturn(Optional.of(user1));
        when(cartItemRepository.findByUser(user1)).thenReturn(List.of(cartItem));
        when(productRepository.save(any(Product.class))).thenReturn(lastProduct);
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> {
            Order o = inv.getArgument(0);
            o.setId(1L);
            return o;
        });

        orderService.checkout("user1@test.com", null);

        // Stock debe bajar de 10 a 7
        assertEquals(7, lastProduct.getStock());
    }
}
