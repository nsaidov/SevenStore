package com.sevenstore.service;

import com.sevenstore.entity.Shop;
import com.sevenstore.repository.ShopRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Service
public class ShopService {

    private final ShopRepository shopRepository;

    public ShopService(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    @Transactional
    public StringJoiner createShop(@RequestBody Shop shop) {
        StringJoiner resultOfValidation = validateInputData(shop);
        if (resultOfValidation.length() > 0) {
            return resultOfValidation;
        }

        shop.setSortOrder(shopRepository.count() + 1);
        if (shop.getIsActive() == null) {
            shop.setIsActive(true);
        }

        shopRepository.save(shop);
        return resultOfValidation;
    }

    @Transactional
    public String editShop(@RequestBody Shop shop){

        StringJoiner resultOfValidation = new StringJoiner("; ");

        if (shop.getId() == null) {
            resultOfValidation.add("ID не указано");
            return resultOfValidation.toString();
        }

        Shop modifiedShop = shopRepository.findById(shop.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (shop.getName() != null){
            if (shop.getName().isEmpty()) {
                resultOfValidation.add("Название не заполнено");
            }
            else {
                modifiedShop.setName(shop.getName());
            }
        }

        if (shop.getSortOrder() != null) {
            modifiedShop.setSortOrder(shop.getSortOrder());
        }

        if (shop.getIsActive() != null) {
            modifiedShop.setIsActive(shop.getIsActive());
        }

        if (resultOfValidation.length() > 0){
            return  resultOfValidation.toString();
        }

        return "";
    }

    @Transactional
    public void editOrders(@RequestBody Map<String, List<Long>> orders){
        for (int i=1; i <= orders.get("orders").size(); i++) {
            Shop modifiedShop = shopRepository.findById(orders.get("orders").get(i-1))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            modifiedShop.setSortOrder((long) i);
            shopRepository.save(modifiedShop);
        }
    }

    private StringJoiner validateInputData(Shop shop) {
        StringJoiner resultOfValidation = new StringJoiner("; ");
        if (shop.getName() == null || shop.getName().isEmpty()) {
            return resultOfValidation.add("Название не заполнено");
        }

        return  resultOfValidation;
    }
}
