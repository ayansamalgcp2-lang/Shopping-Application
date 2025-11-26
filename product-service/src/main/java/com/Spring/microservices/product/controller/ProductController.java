package com.Spring.microservices.product.controller;

import com.Spring.microservices.product.dto.ProductRequest;
import com.Spring.microservices.product.dto.ProductResponse;
import com.Spring.microservices.product.model.Product;
import com.Spring.microservices.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;  // Add this import
import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*")  // Changed from origins = "*" to originPatterns = "*" // Enable CORS for all origins
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@Valid @RequestBody ProductRequest productRequest){  // Added @Valid
       return productService.createProduct(productRequest);

    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts(){

        return productService.getAllProducts();

    }

}
