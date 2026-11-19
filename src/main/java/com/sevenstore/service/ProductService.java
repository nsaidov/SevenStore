package com.sevenstore.service;

import com.sevenstore.entity.Product;
import com.sevenstore.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.StringJoiner;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Transactional
    public StringJoiner createProduct(@RequestBody Product product){

        StringJoiner resultOfRequest = new StringJoiner("; ");

        if (product.getName() == null){
            resultOfRequest.add("Название номенклатуры не заполнено");
        }

        if (product.getUnitSale() == null){
            resultOfRequest.add("Единица измерения продажи не заполнена");
        }

        if (product.getUnitPurchase() == null){
            resultOfRequest.add("Единица измерения закупки не заполнена");
        }

        if (resultOfRequest.length() > 0){
            return resultOfRequest;
        } else if (product.getIsActive() == null) {
            product.setIsActive(true);
        }

        product.setCode(String.format("%05d",productRepository.count()+1));

        productRepository.save(product);

        return resultOfRequest;
    }
}
