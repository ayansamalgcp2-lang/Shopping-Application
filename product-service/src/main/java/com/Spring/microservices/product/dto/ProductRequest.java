package com.Spring.microservices.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * DTO for Product Request
 * Updated: Testing Docker cache optimization
 */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductRequest {
    @NotBlank(message = "Product name is required")  // ← Add this    
    private static final String VERSION = "1.0.2";    
    private String id;
    private String name;
    private String description;
    @NotNull(message = "Product price is required")  // ← Add this
    @DecimalMin(value = "0.0", inclusive = true)  // ← Add this    
    private BigDecimal price;
    public String getVersion() {
        return VERSION;
    }
}
