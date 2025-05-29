package com.example.ecommerce.service;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    public void testGetProductById() {
        Product product = new Product();
        product.setId(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        
        Product found = productService.getProductById(1L);
        assertEquals(1L, found.getId());
    }

    @Test
    public void testSaveProduct() {
        Product product = new Product();
        product.setName("Test Product");
        when(productRepository.save(any(Product.class))).thenReturn(product);
        
        Product saved = productService.saveProduct(product, null);
        assertEquals("Test Product", saved.getName());
    }
}