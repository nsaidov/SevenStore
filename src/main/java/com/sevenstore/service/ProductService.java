package com.sevenstore.service;

import com.sevenstore.entity.Product;
import com.sevenstore.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Transactional
    public Product createProduct(@RequestBody Product product){
        if (product.getName() == null){
            throw new IllegalArgumentException("Название номенклатуры не заполнено");
        }

        if (product.getUnitSale() == null){
            throw new IllegalArgumentException("Единица измерения продажи не заполнена");
        }

        if (product.getUnitPurchase() == null){
            throw new IllegalArgumentException("Единица измерения закупки не заполнена");
        }

        return productRepository.save(product);
    }
}
