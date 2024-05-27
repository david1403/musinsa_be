package com.musinsa.service;

import com.musinsa.common.ErrorCode;
import com.musinsa.common.MusinsaApiException;
import com.musinsa.model.entity.Brand;
import com.musinsa.repository.BrandRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BrandServiceTest {
    @Mock
    private BrandRepository brandRepository;
    @InjectMocks
    private BrandService brandService;

    @Test
    void findAllBrands() {
        //given
        when(brandRepository.findAll()).thenReturn(new ArrayList<>());

        //when
        List<Brand> brands = brandService.findAllBrands();

        //then
        assertEquals(0, brands.size());
        verify(brandRepository, times(1)).findAll();
    }

    @Test
    void findByBrandName__brandExists() {
        //given
        when(brandRepository.findByBrandName("test")).thenReturn(Optional.ofNullable(new Brand()));

        //when
        Brand brand = brandService.findByBrandName("test");

        //then
        assertNotNull(brand);
        assertEquals(0, brand.getBrandId());
    }

    @Test
    void findByBrandName__brandDoesNotExist() {
        //given
        when(brandRepository.findByBrandName("test")).thenReturn(Optional.empty());

        //when + then
        MusinsaApiException exception = assertThrows(MusinsaApiException.class, () -> brandService.findByBrandName("test"));

        assertEquals(exception.getErrorCode(), ErrorCode.BRAND_NOT_FOUND_BY_BRAND_NAME);
    }

    @Test
    void registerBrand() {
        //given
        when(brandRepository.save(any())).thenReturn(new Brand());

        //when
        Brand brand = brandService.registerBrand("test");

        //then
        assertEquals(0, brand.getBrandId());
        verify(brandRepository, times(1)).save(any());
    }

    @Test
    void hasDuplicateName__alreadyExists__thenTrue() {
        //given
        when(brandRepository.findByBrandName("test")).thenReturn(Optional.ofNullable(new Brand()));

        //when
        boolean hasDuplicateBrandName = brandService.hasDuplicateBrandName("test");

        //then
        assertTrue(hasDuplicateBrandName);
    }

    @Test
    void hasDuplicateName__notExist__thenFalse() {
        //given
        when(brandRepository.findByBrandName("test")).thenReturn(Optional.ofNullable(null));

        //when
        boolean hasDuplicateBrandName = brandService.hasDuplicateBrandName("test");

        //then
        assertFalse(hasDuplicateBrandName);
    }
}