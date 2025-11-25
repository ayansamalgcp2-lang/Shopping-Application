package com.Spring.microservices.product.service;

import com.Spring.microservices.product.dto.ProductRequest;
import com.Spring.microservices.product.dto.ProductResponse;
import com.Spring.microservices.product.model.Product;
import com.Spring.microservices.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Product Service Unit Tests")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private ProductRequest productRequest;
    private Product product;

    @BeforeEach
    void setUp() {
        // Setup test data
        productRequest = ProductRequest.builder()
                .name("iPhone 15")
                .description("Latest iPhone model")
                .price(BigDecimal.valueOf(999.99))
                .build();

        product = Product.builder()
                .id("1")
                .name("iPhone 15")
                .description("Latest iPhone model")
                .price(BigDecimal.valueOf(999.99))
                .build();
    }

    @Test
    @DisplayName("Should create product successfully")
    void shouldCreateProductSuccessfully() {
        // Given
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // When
        ProductResponse response = productService.createProduct(productRequest);

        // Then
        assertNotNull(response);
        assertEquals("iPhone 15", response.getName());
        assertEquals("Latest iPhone model", response.getDescription());
        assertEquals(BigDecimal.valueOf(999.99), response.getPrice());
        
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Should get all products successfully")
    void shouldGetAllProductsSuccessfully() {
        // Given
        Product product2 = Product.builder()
                .id("2")
                .name("MacBook Pro")
                .description("M3 chip")
                .price(BigDecimal.valueOf(1999.99))
                .build();

        List<Product> products = Arrays.asList(product, product2);
        when(productRepository.findAll()).thenReturn(products);

        // When
        List<ProductResponse> responses = productService.getAllProducts();

        // Then
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertThat(responses)
                .extracting(ProductResponse::getName)
                .containsExactlyInAnyOrder("iPhone 15", "MacBook Pro");
        
        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no products exist")
    void shouldReturnEmptyListWhenNoProducts() {
        // Given
        when(productRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<ProductResponse> responses = productService.getAllProducts();

        // Then
        assertNotNull(responses);
        assertTrue(responses.isEmpty());
        assertEquals(0, responses.size());
    }

    @Test
    @DisplayName("Should handle null product name")
    void shouldHandleNullProductName() {
        // Given
        productRequest.setName(null);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(productRequest);
        });
    }

    @Test
    @DisplayName("Should handle negative price")
    void shouldHandleNegativePrice() {
        // Given
        productRequest.setPrice(BigDecimal.valueOf(-10.00));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(productRequest);
        });
    }

    @Test
    @DisplayName("Should map product to response correctly")
    void shouldMapProductToResponseCorrectly() {
        // Given
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));

        // When
        List<ProductResponse> responses = productService.getAllProducts();

        // Then
        ProductResponse response = responses.get(0);
        assertEquals(product.getId(), response.getId());
        assertEquals(product.getName(), response.getName());
        assertEquals(product.getDescription(), response.getDescription());
        assertEquals(product.getPrice(), response.getPrice());
    }
}