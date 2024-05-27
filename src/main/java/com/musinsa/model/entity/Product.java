package com.musinsa.model.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
@NoArgsConstructor
@Getter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Column(name = "price")
    private long price;

    @Builder
    public Product(long productId, long price, Category category, Brand brand) {
        this.productId = productId;
        this.price = price;
        this.category = category;
        this.brand = brand;
    }

    public void modifyPrice(long price) {
        this.price = price;
    }
}
