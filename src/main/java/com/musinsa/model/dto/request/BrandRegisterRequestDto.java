package com.musinsa.model.dto.request;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class BrandRegisterRequestDto {
    private String brandName;
    private Map<String, List<Long>> products;
}
