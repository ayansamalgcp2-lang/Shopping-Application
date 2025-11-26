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

    private ProductRequest validProductRequest;
    private Product savedProduct;

    @BeforeEach
    void setUp() {
        validProductRequest = ProductRequest.builder()
                .name("iPhone 15")
                .description("Latest iPhone model")
                .price(BigDecimal.valueOf(999.99))
                .build();

        savedProduct = Product.builder()
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
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // When
        ProductResponse response = productService.createProduct(validProductRequest);

        // Then
        assertNotNull(response);
        assertEquals("1", response.getId());
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

        when(productRepository.findAll()).thenReturn(Arrays.asList(savedProduct, product2));

        // When
        List<ProductResponse> products = productService.getAllProducts();

        // Then
        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals("iPhone 15", products.get(0).getName());
        assertEquals("MacBook Pro", products.get(1).getName());
        
        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should handle null product name")
    void shouldHandleNullProductName() {
        // Given
        ProductRequest invalidRequest = ProductRequest.builder()
                .name(null)  // Null name
                .description("Test description")
                .price(BigDecimal.valueOf(100.00))
                .build();

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> productService.createProduct(invalidRequest)
        );
        
        assertTrue(exception.getMessage().contains("name"));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Should handle negative price")
    void shouldHandleNegativePrice() {
        // Given
        ProductRequest invalidRequest = ProductRequest.builder()
                .name("Test Product")
                .description("Test description")
                .price(BigDecimal.valueOf(-50.00))  // Negative price
                .build();

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> productService.createProduct(invalidRequest)
        );
        
        assertTrue(exception.getMessage().contains("price"));
        assertTrue(exception.getMessage().contains("negative"));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Should handle empty product name")
    void shouldHandleEmptyProductName() {
        // Given
        ProductRequest invalidRequest = ProductRequest.builder()
                .name("")  // Empty name
                .description("Test description")
                .price(BigDecimal.valueOf(100.00))
                .build();

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> productService.createProduct(invalidRequest)
        );
        
        assertTrue(exception.getMessage().contains("name"));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Should handle whitespace-only product name")
    void shouldHandleWhitespaceProductName() {
        // Given
        ProductRequest invalidRequest = ProductRequest.builder()
                .name("   ")  // Whitespace only
                .description("Test description")
                .price(BigDecimal.valueOf(100.00))
                .build();

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> productService.createProduct(invalidRequest)
        );
        
        assertTrue(exception.getMessage().contains("name"));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Should handle null price")
    void shouldHandleNullPrice() {
        // Given
        ProductRequest invalidRequest = ProductRequest.builder()
                .name("Test Product")
                .description("Test description")
                .price(null)  // Null price
                .build();

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> productService.createProduct(invalidRequest)
        );
        
        assertTrue(exception.getMessage().contains("price"));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Should handle zero price")
    void shouldHandleZeroPrice() {
        // Given - Zero price should be allowed
        ProductRequest requestWithZeroPrice = ProductRequest.builder()
                .name("Free Product")
                .description("Test description")
                .price(BigDecimal.ZERO)
                .build();

        Product productWithZeroPrice = Product.builder()
                .id("1")
                .name("Free Product")
                .description("Test description")
                .price(BigDecimal.ZERO)
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(productWithZeroPrice);

        // When
        ProductResponse response = productService.createProduct(requestWithZeroPrice);

        // Then
        assertNotNull(response);
        assertEquals(BigDecimal.ZERO, response.getPrice());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Should handle null product request")
    void shouldHandleNullProductRequest() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> productService.createProduct(null)
        );
        
        assertTrue(exception.getMessage().contains("request"));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Should return empty list when no products exist")
    void shouldReturnEmptyListWhenNoProducts() {
        // Given
        when(productRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<ProductResponse> products = productService.getAllProducts();

        // Then
        assertNotNull(products);
        assertTrue(products.isEmpty());
        verify(productRepository, times(1)).findAll();
    }
}