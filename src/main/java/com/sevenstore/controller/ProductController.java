package com.sevenstore.controller;

import com.sevenstore.entity.Product;
import com.sevenstore.repository.ProductRepository;
import com.sevenstore.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/procurement/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductService productService;

    public ProductController(ProductRepository productRepository, ProductService productService){
        this.productRepository = productRepository;
        this.productService = productService;
    }

    @GetMapping()
    public List<Product> findProducts() {
        return productRepository.findAll();
    }

    @PostMapping()
    public ResponseEntity<Void> createProduct(@RequestBody Product product){
        Product savedProduct = productService.createProduct(product);
        return ResponseEntity.ok().build();
    }
}
