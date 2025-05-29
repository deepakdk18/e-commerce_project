package com.example.ecommerce.service;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.User;

import java.util.List;

public interface OrderService {
    Order createOrder(User user, String shippingAddress);
    Order getOrderById(Long id);
    List<Order> getOrdersByUser(User user);
    void cancelOrder(Long id);
}