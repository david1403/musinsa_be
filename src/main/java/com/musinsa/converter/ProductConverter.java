package com.musinsa.converter;

import com.musinsa.model.dto.common.ProductDto;
import com.musinsa.model.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductConverter {

    private final BrandConverter brandConverter;
    private final CategoryConverter categoryConverter;

    public ProductDto convertToDto(Product product) {
        if (product == null) {
            return null;
        }
        return ProductDto.builder()
                .productId(product.getProductId())
                .price(product.getPrice())
                .brandDto(brandConverter.convertToDto(product.getBrand()))
                .categoryDto(categoryConverter.convertToDto(product.getCategory()))
                .build();
    }

    public Product convertToEntity(ProductDto productDto) {
        if (productDto == null) {
            return null;
        }

        return Product.builder()
                .productId(productDto.getProductId())
                .price(productDto.getPrice())
                .brand(brandConverter.convertToEntity(productDto.getBrandDto()))
                .category(categoryConverter.convertToEntity(productDto.getCategoryDto()))
                .build();
    }
}
