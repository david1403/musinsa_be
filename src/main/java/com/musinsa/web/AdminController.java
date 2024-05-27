package com.musinsa.web;

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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final BrandService brandService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final ProductConverter productConverter;
    private final BrandRegistrationService brandRegistrationService;

    @PostMapping("/register/product")
    public ResponseEntity<ProductDto> registerProduct(@RequestBody ProductRegisterRequestDto productRegisterRequestDto) {
        Brand brand = brandService.findByBrandName(productRegisterRequestDto.getBrandName());
        Category category = categoryService.findByCategoryName(productRegisterRequestDto.getCategoryName());
        long price = productRegisterRequestDto.getPrice();

        Product product = Product.builder()
                .category(category)
                .brand(brand)
                .price(price)
                .build();

        return ResponseEntity.ok(productConverter.convertToDto(productService.registerProduct(product)));
    }

    @PostMapping("/modify/product")
    public ResponseEntity<ProductDto> modifyProduct(@RequestBody ProductModifyRequestDto productModifyRequestDto) {
        Product modifiedProduct = productService.modifyProduct(productModifyRequestDto.getProductId(), productModifyRequestDto.getModifiedPrice());
        return ResponseEntity.ok(productConverter.convertToDto(modifiedProduct));
    }

    @PostMapping("/delete/product/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(String.format("Successfully deleted product with productId: %s", productId));
    }

    @PostMapping("/register/brand")
    public ResponseEntity<String> registerBrand(@RequestBody BrandRegisterRequestDto brandRegisterRequestDto) {
        brandRegistrationService.registerBrand(brandRegisterRequestDto);
        return ResponseEntity.ok(String.format("Successfully added brand with name: %s", brandRegisterRequestDto.getBrandName()));
    }
}
