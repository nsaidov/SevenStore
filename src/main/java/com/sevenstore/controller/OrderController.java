package com.sevenstore.controller;

import com.sevenstore.entity.Order;
import com.sevenstore.entity.OrderRequest;
import com.sevenstore.security.CheckApiKey;
import com.sevenstore.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/procurement/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @CheckApiKey
    @PostMapping
//    public ResponseEntity<?> createOrder(@RequestBody Order order){
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest){

        String resultOfRequest = orderService.createOrder(orderRequest);

        if (!resultOfRequest.isEmpty()) {
            return ResponseEntity.badRequest().body(resultOfRequest);
        }

        return ResponseEntity.ok().build();
    }
}
