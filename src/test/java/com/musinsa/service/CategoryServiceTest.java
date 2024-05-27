package com.musinsa.service;

import com.musinsa.common.ErrorCode;
import com.musinsa.common.MusinsaApiException;
import com.musinsa.model.entity.Category;
import com.musinsa.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryService categoryService;

    @Test
    void findAllCategories() {
        //given
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(new Category()));

        //when
        List<Category> allCategories = categoryService.findAllCategories();

        //then
        assertEquals(1, allCategories.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void findByCategoryName__categoryExists() {
        //given
        when(categoryRepository.findByCategoryName("test")).thenReturn(Optional.ofNullable(new Category()));

        //when
        Category category = categoryService.findByCategoryName("test");

        //then
        assertNotNull(category);
    }

    @Test
    void findByCategoryName__doesNotExist() {
        //given
        when(categoryRepository.findByCategoryName("test")).thenReturn(Optional.empty());

        //when + then
        MusinsaApiException exception = assertThrows(MusinsaApiException.class, () -> categoryService.findByCategoryName("test"));

        //then
        assertEquals(exception.getErrorCode(), ErrorCode.CATEGORY_NOT_FOUND_BY_CATEGORY_NAME);
    }
}