package com.musinsa.service;

import com.musinsa.common.ErrorCode;
import com.musinsa.common.MusinsaApiException;
import com.musinsa.model.entity.Brand;
import com.musinsa.model.entity.Category;
import com.musinsa.model.entity.Product;
import com.musinsa.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public List<Product> findCheapestProductsByCategory(@NonNull Category category) {
        List<Product> productsByCategory = category.getProductsByCategory();

        long minPrice = productsByCategory.stream().mapToLong(Product::getPrice).min().orElseThrow(
                () -> new MusinsaApiException(ErrorCode.NO_PRODUCTS_IN_CATEGORY)
        );

        return productsByCategory.stream().filter(product -> product.getPrice() == minPrice).toList();
    }

    @Transactional
    public List<Product> findMostExpensiveProductsByCategory(@NonNull Category category) {
        List<Product> productsByCategory = category.getProductsByCategory();

        long minPrice = productsByCategory.stream().mapToLong(Product::getPrice).max().orElseThrow(
                () -> new MusinsaApiException(ErrorCode.NO_PRODUCTS_IN_CATEGORY)
        );

        return productsByCategory.stream().filter(product -> product.getPrice() == minPrice).toList();
    }

    @Transactional
    public List<Product> findCheapestProductsByBrand(@NonNull Brand brand) {
        List<Product> productsByBrand = brand.getProductsByBrand();

        Map<Category, List<Product>> productMap = productsByBrand.stream().collect(Collectors.groupingBy(Product::getCategory));

        return productMap.keySet().stream()
                .map(category -> findCheapestProduct(productMap.get(category)))
                .collect(Collectors.toList());
    }

    public Product registerProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public Product modifyProduct(long productId, long price) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new MusinsaApiException(ErrorCode.PRODUCT_NOT_FOUND_BY_PRODUCT_ID)
        );
        product.modifyPrice(price);
        return product;
    }

    @Transactional
    public void deleteProduct(long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new MusinsaApiException(ErrorCode.PRODUCT_NOT_FOUND_BY_PRODUCT_ID)
        );
        List<Product> sameCategoryAndBrandProducts = productRepository.findAllByBrandAndCategory(product.getBrand(), product.getCategory());

        if (sameCategoryAndBrandProducts.size() == 1) {
            throw new MusinsaApiException(ErrorCode.PRODUCT_DELETION_NOT_AVAILABLE);
        }
        productRepository.delete(product);
    }

    private Product findCheapestProduct(@NonNull List<Product> products) {
        return products.stream().min(Comparator.comparingLong(Product::getPrice)).orElseThrow();
    }
}
