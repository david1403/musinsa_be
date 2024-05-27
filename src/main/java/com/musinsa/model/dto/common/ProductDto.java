package com.musinsa.model.dto.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDto {
    private long productId;
    private long price;
    private CategoryDto categoryDto;
    private BrandDto brandDto;
}
