package com.musinsa.service;

import com.musinsa.common.ErrorCode;
import com.musinsa.common.MusinsaApiException;
import com.musinsa.model.entity.Brand;
import com.musinsa.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;

    public List<Brand> findAllBrands() {
        return brandRepository.findAll();
    }

    public Brand findByBrandName(String brandName) {
        return brandRepository.findByBrandName(brandName).orElseThrow(
                () -> new MusinsaApiException(ErrorCode.BRAND_NOT_FOUND_BY_BRAND_NAME)
        );
    }

    public Brand registerBrand(String brandName) {
        Brand brand = Brand.builder().brandName(brandName).build();
        return brandRepository.save(brand);
    }

    public boolean hasDuplicateBrandName(String brandName) {
        return brandRepository.findByBrandName(brandName).isPresent();
    }
}
