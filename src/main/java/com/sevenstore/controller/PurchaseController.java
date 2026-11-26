package com.sevenstore.controller;

import com.sevenstore.entity.PurchaseRequest;
import com.sevenstore.security.CheckApiKey;
import com.sevenstore.service.PurchaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/procurement/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @CheckApiKey
    @PatchMapping
    public ResponseEntity<?> editPurchase(@RequestBody PurchaseRequest purchaseRequest) {

        String resultOfRequest = purchaseService.editPurchase(purchaseRequest);

        if (!resultOfRequest.isEmpty()) {
            return ResponseEntity.badRequest().body(resultOfRequest);
        }

        return ResponseEntity.ok().build();
    }
}
