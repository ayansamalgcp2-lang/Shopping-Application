package com.Spring.microservices.product.repository;

import com.Spring.microservices.product.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Testcontainers
@DisplayName("Product Repository Tests")
class ProductRepositoryTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0")
            .withExposedPorts(27017);

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();

        product = Product.builder()
                .name("iPhone 15")
                .description("Latest iPhone")
                .price(BigDecimal.valueOf(999.99))
                .build();
    }

    @Test
    @DisplayName("Should save product to database")
    void shouldSaveProduct() {
        // When
        Product savedProduct = productRepository.save(product);

        // Then
        assertNotNull(savedProduct.getId());
        assertEquals("iPhone 15", savedProduct.getName());
        assertEquals("Latest iPhone", savedProduct.getDescription());
        assertEquals(BigDecimal.valueOf(999.99), savedProduct.getPrice());
    }

    @Test
    @DisplayName("Should find all products")
    void shouldFindAllProducts() {
        // Given
        Product product2 = Product.builder()
                .name("MacBook Pro")
                .description("M3 chip")
                .price(BigDecimal.valueOf(1999.99))
                .build();

        productRepository.save(product);
        productRepository.save(product2);

        // When
        List<Product> products = productRepository.findAll();

        // Then
        assertEquals(2, products.size());
        assertThat(products)
                .extracting(Product::getName)
                .containsExactlyInAnyOrder("iPhone 15", "MacBook Pro");
    }

    @Test
    @DisplayName("Should find product by id")
    void shouldFindProductById() {
        // Given
        Product savedProduct = productRepository.save(product);

        // When
        Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());

        // Then
        assertTrue(foundProduct.isPresent());
        assertEquals("iPhone 15", foundProduct.get().getName());
    }

    @Test
    @DisplayName("Should delete product")
    void shouldDeleteProduct() {
        // Given
        Product savedProduct = productRepository.save(product);

        // When
        productRepository.deleteById(savedProduct.getId());

        // Then
        Optional<Product> deletedProduct = productRepository.findById(savedProduct.getId());
        assertFalse(deletedProduct.isPresent());
    }

    @Test
    @DisplayName("Should return empty list when no products exist")
    void shouldReturnEmptyListWhenNoProducts() {
        // When
        List<Product> products = productRepository.findAll();

        // Then
        assertTrue(products.isEmpty());
    }
}