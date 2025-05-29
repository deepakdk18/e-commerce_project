package com.example.ecommerce.service;

import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public ShoppingCartServiceImpl(CartRepository cartRepository, 
                                 CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public Cart addToCart(Product product, int quantity, User user) {
        Cart cart = getOrCreateCart(user);
        
        Optional<CartItem> existingItem = cart.getCartItems().stream()
            .filter(item -> item.getProduct().getId().equals(product.getId()))
            .findFirst();
        
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setCart(cart);
            cart.addCartItem(newItem);
            cartItemRepository.save(newItem);
        }
        
        return cartRepository.save(cart);
    }

    @Override
    public Cart updateCartItem(Product product, int quantity, User user) {
        Cart cart = getCartByUser(user);
        
        cart.getCartItems().stream()
            .filter(item -> item.getProduct().getId().equals(product.getId()))
            .findFirst()
            .ifPresent(item -> {
                item.setQuantity(quantity);
                cartItemRepository.save(item);
            });
        
        return cartRepository.save(cart);
    }

    @Override
    public Cart removeFromCart(Product product, User user) {
        Cart cart = getCartByUser(user);
        
        cart.getCartItems().stream()
            .filter(item -> item.getProduct().getId().equals(product.getId()))
            .findFirst()
            .ifPresent(item -> {
                cart.removeCartItem(item);
                cartItemRepository.delete(item);
            });
        
        return cartRepository.save(cart);
    }

    @Override
    public Cart getCartByUser(User user) {
        return getOrCreateCart(user);
    }

    @Override
    public void clearCart(User user) {
        Cart cart = getCartByUser(user);
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
            .orElseGet(() -> {
                Cart newCart = new Cart();
                newCart.setUser(user);
                return cartRepository.save(newCart);
            });
    }
}