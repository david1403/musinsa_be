package com.musinsa.web;

import com.musinsa.converter.BrandConverter;
import com.musinsa.converter.CategoryConverter;
import com.musinsa.converter.ProductConverter;
import com.musinsa.model.dto.common.ProductDto;
import com.musinsa.model.dto.request.BrandRegisterRequestDto;
import com.musinsa.model.dto.request.ProductModifyRequestDto;
import com.musinsa.model.dto.request.ProductRegisterRequestDto;
import com.musinsa.model.entity.Brand;
import com.musinsa.model.entity.Category;
import com.musinsa.model.entity.Product;
import com.musinsa.service.BrandRegistrationService;
import com.musinsa.service.BrandService;
import com.musinsa.service.CategoryService;
import com.musinsa.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {
    @Mock
    private BrandService brandService;
    @Mock
    private CategoryService categoryService;
    @Mock
    private ProductService productService;
    @Spy
    private ProductConverter productConverter = new ProductConverter(spy(BrandConverter.class), spy(CategoryConverter.class));
    @Mock
    private BrandRegistrationService brandRegistrationService;
    @InjectMocks
    private AdminController adminController;

    @Test
    void registerProduct() {
        //given:
        when(brandService.findByBrandName(any())).thenReturn(new Brand());
        when(categoryService.findByCategoryName(any())).thenReturn(new Category());

        //when
        ResponseEntity<ProductDto> response = adminController.registerProduct(new ProductRegisterRequestDto());

        //then
        verify(productService, times(1)).registerProduct(any());
        verify(productConverter, times(1)).convertToDto(any());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void modifyProduct() {
        //given
        when(productService.modifyProduct(anyLong(), anyLong())).thenReturn(new Product());

        //when
        ResponseEntity<ProductDto> response = adminController.modifyProduct(new ProductModifyRequestDto());

        //then
        verify(productService, times(1)).modifyProduct(anyLong(), anyLong());
        verify(productConverter, times(1)).convertToDto(any());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void deleteProduct() {
        //given
        //when
        ResponseEntity<String> response = adminController.deleteProduct(1L);

        //then
        verify(productService, times(1)).deleteProduct(1L);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), "Successfully deleted product with productId: 1");
    }

    @Test
    void registerBrand() {
        //given
        BrandRegisterRequestDto brandRegisterRequestDto = mock(BrandRegisterRequestDto.class);
        when(brandRegisterRequestDto.getBrandName()).thenReturn("test");

        //when
        ResponseEntity<String> response = adminController.registerBrand(brandRegisterRequestDto);

        //then
        verify(brandRegistrationService, times(1)).registerBrand(any());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), "Successfully added brand with name: test");
    }
}