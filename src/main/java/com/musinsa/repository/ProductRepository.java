package com.musinsa.repository;

import com.musinsa.model.entity.Brand;
import com.musinsa.model.entity.Category;
import com.musinsa.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findFirstByBrandAndCategoryOrderByPrice(Brand brand, Category category);
    List<Product> findAllByBrandAndCategory(Brand brand, Category category);
}
