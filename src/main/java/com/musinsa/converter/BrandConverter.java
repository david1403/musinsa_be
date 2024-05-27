package com.musinsa.converter;

import com.musinsa.model.dto.common.BrandDto;
import com.musinsa.model.entity.Brand;
import org.springframework.stereotype.Component;

@Component
public class BrandConverter {

    public BrandDto convertToDto(Brand brand) {
        if (brand == null) {
            return null;
        }

        return BrandDto.builder()
                .brandId(brand.getBrandId())
                .brandName(brand.getBrandName())
                .build();
    }

    public Brand convertToEntity(BrandDto brandDto) {
        if (brandDto == null) {
            return null;
        }

        return Brand.builder()
                .brandId(brandDto.getBrandId())
                .brandName(brandDto.getBrandName())
                .build();
    }
}
