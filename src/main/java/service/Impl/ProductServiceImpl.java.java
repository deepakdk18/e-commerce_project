package com.example.ecommerce.service;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product saveProduct(Product product, MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                Path path = Paths.get(UPLOAD_DIR + fileName);
                Files.write(path, imageFile.getBytes());
                product.setImageUrl("/uploads/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword);
    }

    @Override
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @Override
    public Page<Product> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? 
            Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return productRepository.findAll(pageable);
    }
}