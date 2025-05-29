package com.example.ecommerce.service;

import com.example.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(Long id);
    Product saveProduct(Product product, MultipartFile imageFile);
    void deleteProduct(Long id);
    List<Product> searchProducts(String keyword);
    List<Product> getProductsByCategory(Long categoryId);
    Page<Product> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
}