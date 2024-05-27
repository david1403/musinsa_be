package com.musinsa.model.dto.response;

import com.musinsa.model.dto.common.ProductDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BrandProductSetResponseDto {
    private String brandName;
    private List<ProductDto> products;
    private long totalPrice;
}
