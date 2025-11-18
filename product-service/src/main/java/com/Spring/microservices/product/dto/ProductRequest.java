package com.Spring.microservices.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for Product Request
 * Updated: Testing Docker cache optimization
 */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductRequest {
    private static final String VERSION = "1.0.2";    
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    public String getVersion() {
        return VERSION;
    }
}
