package com.musinsa.service;

import com.musinsa.common.ErrorCode;
import com.musinsa.common.MusinsaApiException;
import com.musinsa.converter.BrandConverter;
import com.musinsa.converter.CategoryConverter;
import com.musinsa.converter.ProductConverter;
import com.musinsa.model.dto.response.BrandProductSetResponseDto;
import com.musinsa.model.dto.response.ProductsByCategoryResponseDto;
import com.musinsa.model.dto.response.ProductsBySpecificCategoryResponseDto;
import com.musinsa.model.entity.Brand;
import com.musinsa.model.entity.Category;
import com.musinsa.model.entity.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductSelectionServiceTest {
    @Mock
    private BrandService brandService;
    @Mock
    private CategoryService categoryService;
    @Mock
    private ProductService productService;
    @Spy
    private CategoryConverter categoryConverter;
    @Spy
    private ProductConverter productConverter = new ProductConverter(spy(BrandConverter.class), spy(CategoryConverter.class));
    @InjectMocks
    private ProductSelectionService selectionService;

    @Test
    void findCheapestProducts__emptyProductList__exceptionalCase() {
        //given:
        when(categoryService.findAllCategories()).thenReturn(mockCategoryList());
        when(productService.findCheapestProductsByCategory(any())).thenReturn(mockEmptyProductList());

        //when + then
        MusinsaApiException exception = assertThrows(MusinsaApiException.class, () -> selectionService.findCheapestProducts());

        //then
        assertEquals(exception.getErrorCode(), ErrorCode.EMPTY_PRODUCT_LIST);
    }

    @Test
    void findCheapestProducts__validCase() {
        //given:
        when(categoryService.findAllCategories()).thenReturn(mockCategoryList());
        when(productService.findCheapestProductsByCategory(any())).thenReturn(mockValidProductList());

        //when + then
        ProductsByCategoryResponseDto result = selectionService.findCheapestProducts();

        //then
        assertEquals(2000, result.getTotalPrice());
        assertEquals(2, result.getProductInfoMap().keySet().size());
        assertEquals(2, result.getProductInfoMap().get("category1").size());
        assertEquals(2, result.getProductInfoMap().get("category2").size());
    }

    @Test
    void findCheapestBrandSet() {
        //given
        when(brandService.findAllBrands()).thenReturn(mockBrandList());
        when(productService.findCheapestProductsByBrand(any())).thenReturn(mockValidProductList());

        //when
        BrandProductSetResponseDto result = selectionService.findCheapestBrandSet();

        //then
        assertEquals(2000, result.getTotalPrice());
        assertEquals("A", result.getBrandName());
        assertEquals(2, result.getProducts().size());
    }

    @Test
    void findProductsByCategoryName__invalidCategoryName() {
        //given
        when(categoryService.findByCategoryName(any())).thenThrow(new MusinsaApiException(ErrorCode.CATEGORY_NOT_FOUND_BY_CATEGORY_NAME));

        //when + then
        MusinsaApiException exception = assertThrows(MusinsaApiException.class, () -> selectionService.findProductsByCategoryName("상의"));

        //then
        assertEquals(exception.getErrorCode(), ErrorCode.CATEGORY_NOT_FOUND_BY_CATEGORY_NAME);
    }

    @Test
    void findProductsByCategoryName__validCase() {
        //given
        when(categoryService.findByCategoryName(any())).thenReturn(Category.builder().categoryName("category1").build());
        when(productService.findCheapestProductsByCategory(any())).thenReturn(mockValidProductList());
        when(productService.findMostExpensiveProductsByCategory(any())).thenReturn(mockValidProductList());

        //when
        ProductsBySpecificCategoryResponseDto result = selectionService.findProductsByCategoryName("category1");

        //then
        assertEquals("category1", result.getCategoryName());
        assertEquals(2, result.getCheapestProducts().size());
        assertEquals(2, result.getMostExpensiveProducts().size());
    }

    private List<Category> mockCategoryList() {
        return Arrays.asList(
                Category.builder().categoryName("category1").build(),
                Category.builder().categoryName("category2").build()
        );
    }

    private List<Brand> mockBrandList() {
        return Arrays.asList(
                Brand.builder().brandName("A").build()
        );
    }

    private List<Product> mockValidProductList() {
        return Arrays.asList(
                Product.builder().price(1000).build(),
                Product.builder().price(1000).build()
        );
    }

    private List<Product> mockEmptyProductList() {
        return Arrays.asList();
    }
}