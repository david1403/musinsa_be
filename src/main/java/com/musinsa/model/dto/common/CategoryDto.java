package com.musinsa.model.dto.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryDto {
    private long categoryId;
    private String categoryName;
}
