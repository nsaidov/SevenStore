package com.sevenstore.controller;

import com.sevenstore.entity.Shop;
import com.sevenstore.repository.ShopRepository;
import com.sevenstore.security.CheckApiKey;
import com.sevenstore.service.ShopService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@RestController
@RequestMapping("/api/procurement/shop")
public class ShopController {

    private final ShopRepository shopRepository;
    private final ShopService shopService;

    public ShopController(ShopRepository shopRepository, ShopService shopService){
        this.shopRepository = shopRepository;
        this.shopService = shopService;
    }

    @CheckApiKey
    @GetMapping
    public List<Shop> findUnits(){
        return shopRepository.findAll();
    }


    @CheckApiKey
    @PostMapping
    public ResponseEntity<?> createShop(@RequestBody Shop shop){

        StringJoiner resultOfRequest = shopService.createShop(shop);
        if (resultOfRequest.length() > 0){
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, resultOfRequest.toString());
            return  ResponseEntity.badRequest().body(problemDetail);
        }

        return ResponseEntity.ok().build();
    }

    @CheckApiKey
    @PatchMapping
    public ResponseEntity<?> editShop(@RequestBody Shop shop){

        String resultOfRequest = shopService.editShop(shop);
        if (!resultOfRequest.isEmpty()){
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, resultOfRequest);
            return ResponseEntity.badRequest().body(problemDetail);
        }

        return ResponseEntity.ok().build();
    }

    @CheckApiKey
    @PatchMapping("/order")
    public ResponseEntity<?> editOrders(@RequestBody Map<String, List<Long>> orders){
        shopService.editOrders(orders);
        return ResponseEntity.ok().build();
    }
}
