package com.Spring.microservices.product.controller;

import com.Spring.microservices.product.dto.ProductRequest;
import com.Spring.microservices.product.dto.ProductResponse;
import com.Spring.microservices.product.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@DisplayName("Product Controller Unit Tests")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    private ProductRequest productRequest;
    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {
        productRequest = ProductRequest.builder()
                .name("iPhone 15")
                .description("Latest iPhone")
                .price(BigDecimal.valueOf(999.99))
                .build();

        productResponse = ProductResponse.builder()
                .id("1")
                .name("iPhone 15")
                .description("Latest iPhone")
                .price(BigDecimal.valueOf(999.99))
                .build();
    }

    @Test
    @DisplayName("POST /api/product - Should create product")
    void shouldCreateProduct() throws Exception {
        // Given
        when(productService.createProduct(any(ProductRequest.class)))
                .thenReturn(productResponse);

        // When & Then
        mockMvc.perform(post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("iPhone 15"))
                .andExpect(jsonPath("$.description").value("Latest iPhone"))
                .andExpect(jsonPath("$.price").value(999.99));

        verify(productService, times(1)).createProduct(any(ProductRequest.class));
    }

    @Test
    @DisplayName("POST /api/product - Should return 400 for invalid request")
    void shouldReturnBadRequestForInvalidProduct() throws Exception {
        // Given - empty product name
        productRequest.setName("");

        // When & Then
        mockMvc.perform(post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/product - Should return all products")
    void shouldReturnAllProducts() throws Exception {
        // Given
        ProductResponse product2 = ProductResponse.builder()
                .id("2")
                .name("MacBook Pro")
                .description("M3 chip")
                .price(BigDecimal.valueOf(1999.99))
                .build();

        List<ProductResponse> products = Arrays.asList(productResponse, product2);
        when(productService.getAllProducts()).thenReturn(products);

        // When & Then
        mockMvc.perform(get("/api/product")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("iPhone 15"))
                .andExpect(jsonPath("$[1].name").value("MacBook Pro"));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    @DisplayName("GET /api/product - Should return empty array when no products")
    void shouldReturnEmptyArrayWhenNoProducts() throws Exception {
        // Given
        when(productService.getAllProducts()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/product")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("POST /api/product - Should handle missing required fields")
    void shouldHandleMissingRequiredFields() throws Exception {
        // Given - product with missing price
        String invalidJson = "{\"name\":\"iPhone\",\"description\":\"Test\"}";

        // When & Then
        mockMvc.perform(post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle CORS headers")
    void shouldHandleCorsHeaders() throws Exception {
        // When & Then
        mockMvc.perform(options("/api/product")
                .header("Origin", "https://example.com")
                .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isOk())
                .andExpect(header().exists("Access-Control-Allow-Origin"));
    }
}