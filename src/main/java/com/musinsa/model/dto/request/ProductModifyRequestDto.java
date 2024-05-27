package com.musinsa.model.dto.request;

import lombok.Data;

@Data
public class ProductModifyRequestDto {
    private long productId;
    private long modifiedPrice;
}
