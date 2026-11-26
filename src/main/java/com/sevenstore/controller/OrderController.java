package com.sevenstore.controller;

import com.sevenstore.entity.Order;
import com.sevenstore.entity.OrderRequest;
import com.sevenstore.security.CheckApiKey;
import com.sevenstore.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/procurement/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @CheckApiKey
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest){

        String resultOfRequest = orderService.createOrder(orderRequest);

        if (!resultOfRequest.isEmpty()) {
            return ResponseEntity.badRequest().body(resultOfRequest);
        }

        return ResponseEntity.ok().build();
    }

    @CheckApiKey
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable Long orderId) {
        Order order = orderService.getOrder(orderId);
        return ResponseEntity.ok().body(order);
    }

    @CheckApiKey
    @PatchMapping
    public ResponseEntity<?> editOrder(@RequestBody OrderRequest orderRequest) {

        String resultOfRequest = orderService.editOrder(orderRequest);

        if (!resultOfRequest.isEmpty()) {
            return ResponseEntity.badRequest().body(resultOfRequest);
        }

        return ResponseEntity.ok().build();
    }
}
