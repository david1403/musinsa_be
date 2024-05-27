package com.musinsa.service;

import com.musinsa.common.ErrorCode;
import com.musinsa.common.MusinsaApiException;
import com.musinsa.model.entity.Brand;
import com.musinsa.model.entity.Category;
import com.musinsa.model.entity.Product;
import com.musinsa.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    @Test
    void findCheapestProductsByCategory() {
        //given:
        Category category = mock(Category.class);
        when(category.getProductsByCategory()).thenReturn(mockProductLists());

        //when:
        List<Product> result = productService.findCheapestProductsByCategory(category);

        //then:
        assertEquals(2, result.size());
        assertEquals(1000, result.get(0).getPrice());
    }

    @Test
    void findCheapestProductsByCategory__noProductInCategory__exceptionalCase() {
        //given:
        Category category = mock(Category.class);
        when(category.getProductsByCategory()).thenReturn(new ArrayList<>());

        //when:
        MusinsaApiException exception = assertThrows(MusinsaApiException.class, () -> productService.findCheapestProductsByCategory(category));

        //then:
        assertEquals(exception.getErrorCode(), ErrorCode.NO_PRODUCTS_IN_CATEGORY);
    }

    @Test
    void findMostExpensiveProductsByCategory() {
        Category category = mock(Category.class);
        when(category.getProductsByCategory()).thenReturn(mockProductLists());

        //when:
        List<Product> result = productService.findMostExpensiveProductsByCategory(category);

        //then:
        assertEquals(1, result.size());
        assertEquals(1500, result.get(0).getPrice());
    }

    @Test
    void findMostExpensiveProductsByCategory__noProductInCategory__excpetionalCase() {
        //given:
        Category category = mock(Category.class);
        when(category.getProductsByCategory()).thenReturn(new ArrayList<>());

        //when:
        MusinsaApiException exception = assertThrows(MusinsaApiException.class, () -> productService.findMostExpensiveProductsByCategory(category));

        //then:
        assertEquals(exception.getErrorCode(), ErrorCode.NO_PRODUCTS_IN_CATEGORY);
    }

    @Test
    void findCheapestProductsByBrand() {
        //given:
        Brand brand = mock(Brand.class);
        when(brand.getProductsByBrand()).thenReturn(mockProductLists_brand());

        //when:
        List<Product> result = productService.findCheapestProductsByBrand(brand);

        //then:
        assertEquals(2, result.size());
        System.out.println(result.get(0).getCategory());
        assertTrue(Arrays.asList("category1", "category2").containsAll(result.stream().map(Product::getCategory).map(Category::getCategoryName).toList()));
        assertEquals(2000, result.get(0).getPrice() + result.get(1).getPrice());
    }

    @Test
    void registerProduct() {
        //given:
        when(productRepository.save(any())).thenReturn(new Product());

        //when
        Product result = productService.registerProduct(new Product());

        //then
        assertEquals(0, result.getProductId());
        verify(productRepository, times(1)).save(any());
    }

    @Test
    void modifyProduct__productIdExist() {
        //given:
        Product product = mock(Product.class);
        when(productRepository.findById(any())).thenReturn(Optional.ofNullable(product));

        //when:
        productService.modifyProduct(1, 1000);

        //then:
        verify(product, times(1)).modifyPrice(1000);
    }

    @Test
    void modifyProduct__productDoesNotExist() {
        //given:
        Product product = mock(Product.class);
        when(productRepository.findById(any())).thenReturn(Optional.empty());

        //when + then
        MusinsaApiException exception = assertThrows(MusinsaApiException.class, () -> productService.modifyProduct(1, 1000));

        //then:
        assertEquals(exception.getErrorCode(), ErrorCode.PRODUCT_NOT_FOUND_BY_PRODUCT_ID);
        verify(product, times(0)).modifyPrice(anyLong());
    }

    @Test
    void deleteProduct__productExists__canDelete() {
        //given:
        Product product = mock(Product.class);
        when(productRepository.findById(any())).thenReturn(Optional.ofNullable(product));
        when(productRepository.findAllByBrandAndCategory(any(), any())).thenReturn(mockProductLists());

        //when:
        productService.deleteProduct(1);

        //then:
        verify(productRepository, times(1)).delete(any());
    }

    @Test
    void deleteProduct__productExists__cannotDelete() {
        //given:
        Product product = mock(Product.class);
        when(productRepository.findById(any())).thenReturn(Optional.ofNullable(product));
        when(productRepository.findAllByBrandAndCategory(any(), any())).thenReturn(Arrays.asList(product));

        //when:
        MusinsaApiException exception = assertThrows(MusinsaApiException.class, () -> productService.deleteProduct(1));

        //then:
        assertEquals(exception.getErrorCode(), ErrorCode.PRODUCT_DELETION_NOT_AVAILABLE);
        verify(productRepository, times(0)).delete(any());
    }

    @Test
    void deleteProduct__productDoesNotExist() {
        //given:
        Product product = mock(Product.class);
        when(productRepository.findById(any())).thenReturn(Optional.empty());

        //when + then
        MusinsaApiException exception = assertThrows(MusinsaApiException.class, () -> productService.deleteProduct(1));

        //then:
        assertEquals(exception.getErrorCode(), ErrorCode.PRODUCT_NOT_FOUND_BY_PRODUCT_ID);
        verify(product, times(0)).modifyPrice(anyLong());
    }


    private List<Product> mockProductLists() {
        return Arrays.asList(
                Product.builder().price(1000).build(),
                Product.builder().price(1000).build(),
                Product.builder().price(1200).build(),
                Product.builder().price(1300).build(),
                Product.builder().price(1500).build()
        );
    }

    private List<Product> mockProductLists_brand() {
        Category category1 = Category.builder().categoryName("category1").build();
        Category category2 = Category.builder().categoryName("category2").build();

        return Arrays.asList(
                Product.builder().price(1000).category(category1).build(),
                Product.builder().price(1000).category(category2).build(),
                Product.builder().price(1200).category(category1).build(),
                Product.builder().price(1300).category(category2).build(),
                Product.builder().price(1500).category(category1).build()
        );
    }
}