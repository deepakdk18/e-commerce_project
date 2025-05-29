package com.example.ecommerce.service;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;

public interface ShoppingCartService {
    Cart addToCart(Product product, int quantity, User user);
    Cart updateCartItem(Product product, int quantity, User user);
    Cart removeFromCart(Product product, User user);
    Cart getCartByUser(User user);
    void clearCart(User user);
}