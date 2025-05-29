package com.example.ecommerce.service;

import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.CartRepository;
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

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    private User user;
    private Product product;
    private Cart cart;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(100));
        product.setStock(10);

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
    }

    @Test
    void addToCart_NewItem_ShouldAddItem() {
        when(cartRepository.findByUser(user)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart result = shoppingCartService.addToCart(product, 2, user);

        assertNotNull(result);
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void addToCart_ExistingItem_ShouldUpdateQuantity() {
        CartItem existingItem = new CartItem();
        existingItem.setProduct(product);
        existingItem.setQuantity(1);
        cart.addCartItem(existingItem);

        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        shoppingCartService.addToCart(product, 2, user);

        assertEquals(3, existingItem.getQuantity());
        verify(cartItemRepository, times(1)).save(existingItem);
    }

    @Test
    void updateCartItem_ValidProduct_ShouldUpdateQuantity() {
        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(1);
        cart.addCartItem(item);

        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        Cart result = shoppingCartService.updateCartItem(product, 5, user);

        assertEquals(5, item.getQuantity());
        assertNotNull(result);
    }

    @Test
    void removeFromCart_ExistingProduct_ShouldRemoveItem() {
        CartItem item = new CartItem();
        item.setProduct(product);
        cart.addCartItem(item);

        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        Cart result = shoppingCartService.removeFromCart(product, user);

        assertTrue(result.getCartItems().isEmpty());
        verify(cartItemRepository, times(1)).delete(item);
    }

    @Test
    void getCartByUser_NewUser_ShouldCreateNewCart() {
        when(cartRepository.findByUser(user)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart result = shoppingCartService.getCartByUser(user);

        assertNotNull(result);
        assertEquals(user, result.getUser());
    }

    @Test
    void clearCart_ValidUser_ShouldRemoveAllItems() {
        cart.addCartItem(new CartItem());
        cart.addCartItem(new CartItem());

        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        shoppingCartService.clearCart(user);

        assertTrue(cart.getCartItems().isEmpty());
        verify(cartItemRepository, times(2)).delete(any(CartItem.class));
    }
}