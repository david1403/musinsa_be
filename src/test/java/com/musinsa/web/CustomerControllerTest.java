package com.musinsa.web;

import com.musinsa.model.dto.response.BrandProductSetResponseDto;
import com.musinsa.model.dto.response.ProductsByCategoryResponseDto;
import com.musinsa.model.dto.response.ProductsBySpecificCategoryResponseDto;
import com.musinsa.service.ProductSelectionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {
    @Mock
    private ProductSelectionService productSelectionService;
    @InjectMocks
    private CustomerController customerController;

    @Test
    void findCheapestProductInCategory() {
        //given:
        when(productSelectionService.findCheapestProducts()).thenReturn(ProductsByCategoryResponseDto.builder().build());

        //when:
        customerController.findCheapestProductInCategory();

        //then:
        verify(productSelectionService, times(1)).findCheapestProducts();
    }

    @Test
    void findCheapestBrandSet() {
        //given:
        when(productSelectionService.findCheapestBrandSet()).thenReturn(BrandProductSetResponseDto.builder().build());

        //when:
        customerController.findCheapestBrandSet();

        //then:
        verify(productSelectionService, times(1)).findCheapestBrandSet();
    }

    @Test
    void findByCategory() {
        //given:
        when(productSelectionService.findProductsByCategoryName(any())).thenReturn(ProductsBySpecificCategoryResponseDto.builder().build());

        //when:
        customerController.findByCategory("test");

        //then:
        verify(productSelectionService, times(1)).findProductsByCategoryName("test");
    }
}