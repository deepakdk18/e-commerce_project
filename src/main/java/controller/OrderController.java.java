package com.example.ecommerce.controller;

import com.example.ecommerce.model.User;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping
    public String listOrders(Model model, Authentication authentication) {
        User user = userService.getCurrentUser();
        model.addAttribute("orders", orderService.getOrdersByUser(user));
        model.addAttribute("pageTitle", "My Orders - ECommerce");
        return "orders/list";
    }

    @GetMapping("/detail/{id}")
    public String viewOrderDetail(@PathVariable Long id, Model model, Authentication authentication) {
        User user = userService.getCurrentUser();
        model.addAttribute("order", orderService.getOrderById(id));
        model.addAttribute("pageTitle", "Order Details - ECommerce");
        return "orders/detail";
    }

    @PostMapping("/create")
    public String createOrder(@RequestParam String shippingAddress,
                            Authentication authentication) {
        User user = userService.getCurrentUser();
        orderService.createOrder(user, shippingAddress);
        return "redirect:/orders";
    }

    @GetMapping("/cancel/{id}")
    public String cancelOrder(@PathVariable Long id, Authentication authentication) {
        orderService.cancelOrder(id);
        return "redirect:/orders";
    }
}