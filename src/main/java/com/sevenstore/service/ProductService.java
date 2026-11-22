package com.sevenstore.service;

import com.sevenstore.entity.Product;
import com.sevenstore.repository.ProductRepository;
import com.sevenstore.repository.UnitRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.StringJoiner;

@Service
public class ProductService {

    final ProductRepository productRepository;
    final UnitRepository unitRepository;

    public ProductService(ProductRepository productRepository, UnitRepository unitRepository) {
        this.productRepository = productRepository;
        this.unitRepository = unitRepository;
    }

    @Transactional
    public StringJoiner createProduct(@RequestBody Product product) {

        StringJoiner resultOfValidation = validateInputData(product);

        if (resultOfValidation.length() > 0) {
            return resultOfValidation;
        }

        if (product.getIsActive() == null) {
            product.setIsActive(true);
        }

        product.setCode(String.format("%05d",productRepository.count()+1));

        productRepository.save(product);

        return resultOfValidation;
    }

    @Transactional
    public String editProduct(@RequestBody Product product) {

        StringJoiner resultOfValidation = new StringJoiner("; ");

        if (product.getId() == null) {
            resultOfValidation.add("ID не указано");
            return resultOfValidation.toString();
        }

        Product modifiedProduct = productRepository.findById(product.getId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (product.getCode() != null) {
            if (product.getCode().isEmpty()) {
                resultOfValidation.add("Код не заполнен");
            }
            else {
                modifiedProduct.setCode(product.getCode());
            }
        }

        if (product.getName() != null) {
            if (product.getName().isEmpty()) {
                resultOfValidation.add("Название не заполнено");
            }
            else {
                modifiedProduct.setName(product.getName());
            }
        }
        
        if (product.getIsActive() != null) {
            modifiedProduct.setIsActive(product.getIsActive());
        }

        if (product.getUnitSale() != null) {
            if (!unitRepository.existsById(product.getUnitSale().getId())) {
                resultOfValidation.add("Единица измерения продажи указана некорректно");
            }
            else {
                modifiedProduct.setUnitSale(product.getUnitSale());
            }
        }

        if (product.getUnitPurchase() != null) {
            if (!unitRepository.existsById(product.getUnitPurchase().getId())) {
                resultOfValidation.add("Единица измерения закупки указана некорректно");
            }
            else {
                modifiedProduct.setUnitPurchase(product.getUnitPurchase());
            }
        }

        if (resultOfValidation.length() > 0) {
            return resultOfValidation.toString();
        }

        productRepository.save(modifiedProduct);
        return "";
    }

    private StringJoiner validateInputData(Product product) {

        StringJoiner resultOfValidation = new StringJoiner("; ");

        if (product.getName() == null) {
            resultOfValidation.add("Название номенклатуры не заполнено");
        }

        if (product.getUnitSale() == null) {
            resultOfValidation.add("Единица измерения продажи не заполнена");
        }

        if (product.getUnitPurchase() == null) {
            resultOfValidation.add("Единица измерения закупки не заполнена");
        }

        return resultOfValidation;

    }
}
