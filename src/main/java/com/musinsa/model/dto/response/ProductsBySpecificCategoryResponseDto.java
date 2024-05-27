package com.musinsa.model.dto.response;

import com.musinsa.model.dto.common.ProductDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductsBySpecificCategoryResponseDto {
    private String categoryName;
    private List<ProductDto> cheapestProducts;
    private List<ProductDto> mostExpensiveProducts;
}
