package com.example.ecommerce.repository;

import com.example.ecommerce.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql(scripts = "classpath:test-data.sql")
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testFindByCategoryId() {
        List<Product> products = productRepository.findByCategoryId(1L);
        assertThat(products).hasSize(2);
    }

    @Test
    public void testSearchProducts() {
        List<Product> products = productRepository.searchProducts("phone");
        assertThat(products).hasSize(1);
    }
}