package com.example.ecommerce.controller;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.service.ShoppingCartService;
import com.example.ecommerce.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShoppingCartService shoppingCartService;

    @MockBean
    private ProductService productService;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser
    void viewCart_AuthenticatedUser_ShouldReturnCartView() throws Exception {
        User user = new User();
        user.setId(1L);
        
        when(userService.getCurrentUser()).thenReturn(user);
        
        mockMvc.perform(get("/cart"))
               .andExpect(status().isOk())
               .andExpect(view().name("cart/view"));
    }

    @Test
    @WithMockUser
    void addToCart_ValidProduct_ShouldRedirectToCart() throws Exception {
        Product product = new Product();
        product.setId(1L);
        User user = new User();
        
        when(productService.getProductById(1L)).thenReturn(product);
        when(userService.getCurrentUser()).thenReturn(user);
        
        mockMvc.perform(post("/cart/add/1")
                .param("quantity", "2")
                .with(csrf()))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/cart"));
    }

    @Test
    @WithMockUser
    void updateCartItem_ValidRequest_ShouldRedirectToCart() throws Exception {
        Product product = new Product();
        product.setId(1L);
        User user = new User();
        
        when(productService.getProductById(1L)).thenReturn(product);
        when(userService.getCurrentUser()).thenReturn(user);
        
        mockMvc.perform(post("/cart/update/1")
                .param("quantity", "3")
                .with(csrf()))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/cart"));
    }

    @Test
    @WithMockUser
    void removeFromCart_ValidProduct_ShouldRedirectToCart() throws Exception {
        Product product = new Product();
        product.setId(1L);
        User user = new User();
        
        when(productService.getProductById(1L)).thenReturn(product);
        when(userService.getCurrentUser()).thenReturn(user);
        
        mockMvc.perform(get("/cart/remove/1"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/cart"));
    }
}