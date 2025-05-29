package com.example.ecommerce.controller;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.service.ShoppingCartService;
import com.example.ecommerce.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final ProductService productService;
    private final UserService userService;

    public ShoppingCartController(ShoppingCartService shoppingCartService,
                                ProductService productService,
                                UserService userService) {
        this.shoppingCartService = shoppingCartService;
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping
    public String viewCart(Model model, Authentication authentication) {
        User user = userService.getCurrentUser();
        model.addAttribute("cart", shoppingCartService.getCartByUser(user));
        model.addAttribute("pageTitle", "Shopping Cart - ECommerce");
        return "cart/view";
    }

    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId,
                          @RequestParam(defaultValue = "1") int quantity,
                          Authentication authentication) {
        Product product = productService.getProductById(productId);
        User user = userService.getCurrentUser();
        shoppingCartService.addToCart(product, quantity, user);
        return "redirect:/cart";
    }

    @PostMapping("/update/{productId}")
    public String updateCartItem(@PathVariable Long productId,
                               @RequestParam int quantity,
                               Authentication authentication) {
        Product product = productService.getProductById(productId);
        User user = userService.getCurrentUser();
        shoppingCartService.updateCartItem(product, quantity, user);
        return "redirect:/cart";
    }

    @GetMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId,
                               Authentication authentication) {
        Product product = productService.getProductById(productId);
        User user = userService.getCurrentUser();
        shoppingCartService.removeFromCart(product, user);
        return "redirect:/cart";
    }
}