package com.musinsa.service;

import com.musinsa.common.ErrorCode;
import com.musinsa.common.MusinsaApiException;
import com.musinsa.model.dto.request.BrandRegisterRequestDto;
import com.musinsa.model.entity.Category;
import com.musinsa.model.entity.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BrandRegistrationServiceTest {
    @Mock
    private BrandService brandService;
    @Mock
    private CategoryService categoryService;
    @Mock
    private ProductService productService;
    @Mock
    private BrandRegisterRequestDto brandRegisterRequestDto;

    @InjectMocks
    private BrandRegistrationService brandRegistrationService;

    @Test
    void registerBrand__duplicatedBrandName() {
        //given:
        when(brandService.hasDuplicateBrandName(any())).thenReturn(true);

        //when + then
        MusinsaApiException exception = assertThrows(MusinsaApiException.class, () -> brandRegistrationService.registerBrand(brandRegisterRequestDto));

        //then:
        assertEquals(exception.getErrorCode(), ErrorCode.DUPLICATE_BRAND_NAME);
        verify(brandService, times(0)).registerBrand(any());
        verify(productService, times(0)).registerProduct(any());
    }

    @Test
    void registerBrand__insufficientCategories() {
        //given:
        when(brandService.hasDuplicateBrandName(any())).thenReturn(false);
        when(brandRegisterRequestDto.getProducts()).thenReturn(mockValidProducts());
        when(categoryService.findAllCategories()).thenReturn(Arrays.asList(
                Category.builder().categoryName("category1").build(),
                Category.builder().categoryName("category2").build(),
                Category.builder().categoryName("category3").build()
        ));

        //when + then
        MusinsaApiException exception = assertThrows(MusinsaApiException.class, () -> brandRegistrationService.registerBrand(brandRegisterRequestDto));

        //then:
        assertEquals(exception.getErrorCode(), ErrorCode.CATEGORY_MISMATCH_IN_PARAMETER);
        verify(brandService, times(0)).registerBrand(any());
        verify(productService, times(0)).registerProduct(any());
    }

    @Test
    void registerBrand__excessiveCategories() {
        //given:
        when(brandService.hasDuplicateBrandName(any())).thenReturn(false);
        when(brandRegisterRequestDto.getProducts()).thenReturn(mockValidProducts());
        when(categoryService.findAllCategories()).thenReturn(Arrays.asList(
                Category.builder().categoryName("category1").build()
        ));

        //when + then
        MusinsaApiException exception = assertThrows(MusinsaApiException.class, () -> brandRegistrationService.registerBrand(brandRegisterRequestDto));

        //then:
        assertEquals(exception.getErrorCode(), ErrorCode.CATEGORY_MISMATCH_IN_PARAMETER);
        verify(brandService, times(0)).registerBrand(any());
        verify(productService, times(0)).registerProduct(any());
    }

    @Test
    void registerBrand__emptyProducts() {
        //given:
        when(brandService.hasDuplicateBrandName(any())).thenReturn(false);
        when(brandRegisterRequestDto.getProducts()).thenReturn(mockInvalidProducts());
        when(categoryService.findAllCategories()).thenReturn(Arrays.asList(
                Category.builder().categoryName("category1").build(),
                Category.builder().categoryName("category2").build()
        ));

        //when + then
        MusinsaApiException exception = assertThrows(MusinsaApiException.class, () -> brandRegistrationService.registerBrand(brandRegisterRequestDto));

        //then:
        assertEquals(exception.getErrorCode(), ErrorCode.CATEGORY_MISMATCH_IN_PARAMETER);
    }

    @Test
    void registerBrand__happyCase() {
        //given:
        when(brandService.hasDuplicateBrandName(any())).thenReturn(false);
        when(brandRegisterRequestDto.getProducts()).thenReturn(mockValidProducts());
        when(categoryService.findAllCategories()).thenReturn(Arrays.asList(
                Category.builder().categoryName("category1").build(),
                Category.builder().categoryName("category2").build()
        ));

        //when
        brandRegistrationService.registerBrand(brandRegisterRequestDto);

        //then:
        verify(brandService, times(1)).registerBrand(any());
        verify(productService, times(3)).registerProduct(any());
    }

    private Map<String, List<Long>> mockValidProducts() {
        Map<String, List<Long>> productMap = new HashMap<>();
        productMap.put("category1", Arrays.asList(1000L, 2000L));
        productMap.put("category2", Arrays.asList(3000L));
        return productMap;
    }

    private Map<String, List<Long>> mockInvalidProducts() {
        Map<String, List<Long>> productMap = new HashMap<>();
        productMap.put("category1", Arrays.asList(1000L, 2000L));
        productMap.put("category2", Arrays.asList());
        return productMap;
    }
}