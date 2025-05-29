package com.example.ecommerce;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EcommerceApplicationTests {

    @Test
    void contextLoads(ApplicationContext context) {
        assertThat(context).isNotNull();
    }

    @Test
    void mainMethodStartsApplication() {
        EcommerceApplication.main(new String[] {});
    }
}