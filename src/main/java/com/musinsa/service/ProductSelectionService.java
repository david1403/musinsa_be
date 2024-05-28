package com.musinsa.service;

import com.musinsa.common.ErrorCode;
import com.musinsa.common.MusinsaApiException;
import com.musinsa.converter.CategoryConverter;
import com.musinsa.converter.ProductConverter;
import com.musinsa.model.dto.common.ProductDto;
import com.musinsa.model.dto.response.BrandProductSetResponseDto;
import com.musinsa.model.dto.response.ProductsByCategoryResponseDto;
import com.musinsa.model.dto.response.ProductsBySpecificCategoryResponseDto;
import com.musinsa.model.entity.Brand;
import com.musinsa.model.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductSelectionService {
    private final BrandService brandService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final CategoryConverter categoryConverter;
    private final ProductConverter productConverter;

    /**
     * Returns the cheapest products of all categories
     * @return The productMap of categories and products, with the total price.
     */
    public ProductsByCategoryResponseDto findCheapestProducts() {
        ProductsByCategoryResponseDto responseDto = ProductsByCategoryResponseDto.builder()
                .productInfoMap(new HashMap<>())
                .totalPrice(0L)
                .build();

        List<Category> categoryList = categoryService.findAllCategories();
        for (Category category : categoryList) {
            List<ProductDto> products = productService.findCheapestProductsByCategory(category).stream().map(productConverter::convertToDto).toList();

            if (CollectionUtils.isEmpty(products)) {
                throw new MusinsaApiException(ErrorCode.EMPTY_PRODUCT_LIST);
            }

            responseDto.setTotalPrice(responseDto.getTotalPrice() + products.get(0).getPrice());
            responseDto.getProductInfoMap().put(categoryConverter.convertToDto(category).getCategoryName(), products);
        }

        return responseDto;
    }

    /**
     * Returns the cheapest brand, when selecting at least 1 product from every category of that brand.
     * @return the brandName, totalPrice and products included in the brand set.
     */
    public BrandProductSetResponseDto findCheapestBrandSet() {
        BrandProductSetResponseDto response = BrandProductSetResponseDto.builder()
                .brandName(null)
                .products(new ArrayList<>())
                .totalPrice(Long.MAX_VALUE)
                .build();

        List<Brand> brandList = brandService.findAllBrands();

        for (Brand brand : brandList) {
            List<ProductDto> productsInBrand = productService.findCheapestProductsByBrand(brand).stream().map(productConverter::convertToDto).toList();
            long totalBrandPrice = productsInBrand.stream().mapToLong(ProductDto::getPrice).sum();

            if (totalBrandPrice < response.getTotalPrice()) {
                response.setBrandName(brand.getBrandName());
                response.setProducts(productsInBrand);
                response.setTotalPrice(totalBrandPrice);
            }
        }

        return response;
    }

    /**
     * Returns the products in category that are the cheapest and most expensive.
     * @throws MusinsaApiException
     *          if category with categoryName does not exist.
     * @param categoryName
     * @return the categoryName, and the product info
     */
    public ProductsBySpecificCategoryResponseDto findProductsByCategoryName(String categoryName) {
        Category category = categoryService.findByCategoryName(categoryName);

        List<ProductDto> cheapestProducts = productService.findCheapestProductsByCategory(category).stream().map(productConverter::convertToDto).toList();
        List<ProductDto> mostExpensiveProducts = productService.findMostExpensiveProductsByCategory(category).stream().map(productConverter::convertToDto).toList();

        return ProductsBySpecificCategoryResponseDto.builder()
                .categoryName(category.getCategoryName())
                .cheapestProducts(cheapestProducts)
                .mostExpensiveProducts(mostExpensiveProducts)
                .build();
    }
}
