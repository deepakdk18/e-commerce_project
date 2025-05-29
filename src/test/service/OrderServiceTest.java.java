package com.example.ecommerce.service;

import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.OrderItemRepository;
import com.example.ecommerce.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ShoppingCartService shoppingCartService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User user;
    private Cart cart;
    private Product product1, product2;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");
        product1.setPrice(BigDecimal.valueOf(50));
        
        product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setPrice(BigDecimal.valueOf(100));

        CartItem item1 = new CartItem();
        item1.setProduct(product1);
        item1.setQuantity(2);
        
        CartItem item2 = new CartItem();
        item2.setProduct(product2);
        item2.setQuantity(1);

        cart = new Cart();
        cart.setUser(user);
        cart.addCartItem(item1);
        cart.addCartItem(item2);
    }

    @Test
    void createOrder_ValidCart_ShouldCreateOrder() {
        when(shoppingCartService.getCartByUser(user)).thenReturn(cart);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1L);
            return order;
        });

        Order order = orderService.createOrder(user, "123 Main St");

        assertNotNull(order);
        assertEquals(2, order.getOrderItems().size());
        assertEquals(BigDecimal.valueOf(200), order.getTotalAmount());
        assertEquals("PENDING", order.getStatus());
        verify(shoppingCartService, times(1)).clearCart(user);
    }

    @Test
    void getOrderById_ExistingOrder_ShouldReturnOrder() {
        Order order = new Order();
        order.setId(1L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order found = orderService.getOrderById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
    }

    @Test
    void getOrdersByUser_UserWithOrders_ShouldReturnList() {
        Order order1 = new Order();
        order1.setId(1L);
        Order order2 = new Order();
        order2.setId(2L);
        
        when(orderRepository.findByUser(user)).thenReturn(Arrays.asList(order1, order2));

        List<Order> orders = orderService.getOrdersByUser(user);

        assertEquals(2, orders.size());
    }

    @Test
    void cancelOrder_PendingOrder_ShouldUpdateStatus() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus("PENDING");
        
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        orderService.cancelOrder(1L);

        assertEquals("CANCELLED", order.getStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void cancelOrder_NonPendingOrder_ShouldThrowException() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus("SHIPPED");
        
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(RuntimeException.class, () -> orderService.cancelOrder(1L));
    }
}