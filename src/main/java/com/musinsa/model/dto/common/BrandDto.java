package com.musinsa.model.dto.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BrandDto {
    private long brandId;
    private String brandName;
}
