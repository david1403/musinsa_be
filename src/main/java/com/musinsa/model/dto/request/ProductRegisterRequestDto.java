package com.musinsa.model.dto.request;

import lombok.Data;

@Data
public class ProductRegisterRequestDto {
    private String brandName;
    private String categoryName;
    private long price;
}
