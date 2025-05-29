package com.example.ecommerce.service;

import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.OrderItemRepository;
import com.example.ecommerce.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartService shoppingCartService;

    public OrderServiceImpl(OrderRepository orderRepository,
                          OrderItemRepository orderItemRepository,
                          ShoppingCartService shoppingCartService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.shoppingCartService = shoppingCartService;
    }

    @Override
    public Order createOrder(User user, String shippingAddress) {
        Cart cart = shoppingCartService.getCartByUser(user);
        
        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }
        
        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(shippingAddress);
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());
        
        List<OrderItem> orderItems = cart.getCartItems().stream()
            .map(cartItem -> {
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(cartItem.getProduct());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setPrice(cartItem.getProduct().getPrice());
                orderItem.setOrder(order);
                return orderItem;
            })
            .collect(Collectors.toList());
        
        order.setOrderItems(orderItems);
        
        BigDecimal totalAmount = orderItems.stream()
            .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        order.setTotalAmount(totalAmount);
        
        Order savedOrder = orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);
        
        shoppingCartService.clearCart(user);
        
        return savedOrder;
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    @Override
    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found"));
        
        if (!order.getStatus().equals("PENDING")) {
            throw new RuntimeException("Order cannot be cancelled");
        }
        
        order.setStatus("CANCELLED");
        orderRepository.save(order);
    }
}