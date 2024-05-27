package com.musinsa.model.dto.request;

import com.musinsa.model.dto.common.CategoryDto;
import com.musinsa.model.dto.common.ProductDto;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class BrandRegisterRequestDto {
    private String brandName;
    private Map<String, List<Long>> products;
}
