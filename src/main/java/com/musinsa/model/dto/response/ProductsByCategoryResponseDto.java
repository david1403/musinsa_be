package com.musinsa.model.dto.response;

import com.musinsa.model.dto.common.CategoryDto;
import com.musinsa.model.dto.common.ProductDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class ProductsByCategoryResponseDto {
    private final Map<String, List<ProductDto>> productInfoMap;
    private long totalPrice;
}
