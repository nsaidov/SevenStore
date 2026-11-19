package com.sevenstore.controller;

import com.sevenstore.entity.Product;
import com.sevenstore.repository.ProductRepository;
import com.sevenstore.security.CheckApiKey;
import com.sevenstore.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.StringJoiner;

@RestController
@RequestMapping("/api/procurement/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductService productService;

    public ProductController(ProductRepository productRepository, ProductService productService){
        this.productRepository = productRepository;
        this.productService = productService;
    }

    @CheckApiKey
    @GetMapping()
    public List<Product> findProducts() {
        return productRepository.findAll();
    }

    @CheckApiKey
    @PostMapping()
    public ResponseEntity<?> createProduct(@RequestBody Product product){
        StringJoiner resultOfRequest = productService.createProduct(product);

        if (resultOfRequest.length() > 0){
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, resultOfRequest.toString());
            return ResponseEntity.badRequest().body(problemDetail);
        }
        return ResponseEntity.ok().build();
    }
}
