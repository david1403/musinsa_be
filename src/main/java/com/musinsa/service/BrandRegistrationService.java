package com.musinsa.service;

import com.musinsa.common.ErrorCode;
import com.musinsa.common.MusinsaApiException;
import com.musinsa.model.dto.request.BrandRegisterRequestDto;
import com.musinsa.model.entity.Brand;
import com.musinsa.model.entity.Category;
import com.musinsa.model.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandRegistrationService {
    private final BrandService brandService;
    private final CategoryService categoryService;
    private final ProductService productService;

    /**
     * method used when admin registers a new brand
     * @throws MusinsaApiException
     *         if a brand that has brandName with the request already exists,
     *         if productMap does not cover all categories
     * @param brandRegisterRequestDto the registered brandName and productLists of that brand
     */
    @Transactional
    public void registerBrand(BrandRegisterRequestDto brandRegisterRequestDto) {
        if (brandService.hasDuplicateBrandName(brandRegisterRequestDto.getBrandName())) {
            throw new MusinsaApiException(ErrorCode.DUPLICATE_BRAND_NAME);
        }

        if (!containsEveryCategory(brandRegisterRequestDto.getProducts().keySet())) {
            throw new MusinsaApiException(ErrorCode.CATEGORY_MISMATCH_IN_PARAMETER);
        }

        Brand registeredBrand = brandService.registerBrand(brandRegisterRequestDto.getBrandName());

        for (String categoryName : brandRegisterRequestDto.getProducts().keySet()) {
            Category category = categoryService.findByCategoryName(categoryName);
            List<Long> prices = brandRegisterRequestDto.getProducts().get(categoryName);
            if (CollectionUtils.isEmpty(prices)) {
                throw new MusinsaApiException(ErrorCode.CATEGORY_MISMATCH_IN_PARAMETER);
            }
            prices.stream()
                    .map(price -> Product.builder()
                            .price(price)
                            .brand(registeredBrand)
                            .category(category)
                            .build())
                    .forEach(productService::registerProduct);
        }

    }

    private boolean containsEveryCategory(Set<String> categoryNames) {
        Set<String> allCategories = categoryService.findAllCategories().stream().map(Category::getCategoryName).collect(Collectors.toSet());
        return allCategories.size() == categoryNames.size() && allCategories.containsAll(categoryNames);
    }
}
