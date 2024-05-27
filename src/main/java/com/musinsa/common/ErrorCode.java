package com.musinsa.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    //Brand
    BRAND_NOT_FOUND_BY_BRAND_NAME("brand.not.found.by.brand.name"),
    DUPLICATE_BRAND_NAME("duplicate.brand.name"),

    //Category
    CATEGORY_NOT_FOUND_BY_CATEGORY_NAME("category.not.found.by.category.name"),
    CATEGORY_MISMATCH_IN_PARAMETER("category.mismatch.in.parameter"),

    //Product
    NO_PRODUCTS_IN_CATEGORY("product.not.found.in.category"),
    NO_PRODUCTS_IN_BRAND("product.not.found.in.brand"),
    PRODUCT_NOT_FOUND_BY_PRODUCT_ID("product.not.found.by.product.id"),
    PRODUCT_DELETION_NOT_AVAILABLE("product.deletion.not.available"),
    EMPTY_PRODUCT_LIST("empty.product.list"),

    //Common
    FAIL("fail");

    private final String messageKey;
}
