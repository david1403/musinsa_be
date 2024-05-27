package com.musinsa.web;

import com.musinsa.model.dto.response.BrandProductSetResponseDto;
import com.musinsa.model.dto.response.ProductsByCategoryResponseDto;
import com.musinsa.model.dto.response.ProductsBySpecificCategoryResponseDto;
import com.musinsa.service.ProductSelectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final ProductSelectionService productSelectionService;

    @GetMapping("/products/cheapest-in-category")
    public ResponseEntity<ProductsByCategoryResponseDto> findCheapestProductInCategory() {
        ProductsByCategoryResponseDto cheapestProducts = productSelectionService.findCheapestProducts();
        return ResponseEntity.ok(cheapestProducts);
    }

    @GetMapping("/products/cheapest-by-brand")
    public ResponseEntity<BrandProductSetResponseDto> findCheapestBrandSet() {
        BrandProductSetResponseDto cheapestBrandSet = productSelectionService.findCheapestBrandSet();
        return ResponseEntity.ok(cheapestBrandSet);
    }

    @GetMapping("/products/by-category")
    public ResponseEntity<ProductsBySpecificCategoryResponseDto> findByCategory(@RequestParam String categoryName) {
        ProductsBySpecificCategoryResponseDto products = productSelectionService.findProductsByCategoryName(categoryName);
        return ResponseEntity.ok(products);
    }
}
