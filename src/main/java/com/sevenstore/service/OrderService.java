package com.sevenstore.service;

import com.sevenstore.entity.*;
import com.sevenstore.repository.*;
import jakarta.transaction.Transactional;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService {

    private final ShopRepository shopRepository;
    private final ProductRepository productRepository;
    private final UnitRepository unitRepository;
    private final OrderRepository orderRepository;

    public OrderService(ShopRepository shopRepository, ProductRepository productRepository, UnitRepository unitRepository, OrderRepository orderRepository){
        this.shopRepository = shopRepository;
        this.productRepository = productRepository;
        this.unitRepository = unitRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public String createOrder(@RequestBody OrderRequest orderRequest){

        StringJoiner resultOfRequest = new StringJoiner("; ");

        Map<String, Map<Long,?>> orderDependencies = loadOrderDependencies(orderRequest, resultOfRequest);

        Order newOrder = new Order();

        if (resultOfRequest.length() > 0) {
            return resultOfRequest.toString();
        }

        if (orderRequest.getName() == null) {
            newOrder.setName("");
        }
        else {
            newOrder.setName(orderRequest.getName());
        }
        if (orderRequest.getCreatedAt() == null) {
            newOrder.setCreatedAt(LocalDateTime.now());
        }
        else {
            newOrder.setCreatedAt(orderRequest.getCreatedAt());
        }
        if (orderRequest.getIsActive() == null) {
            newOrder.setIsActive(false);
        }
        else {
            newOrder.setIsActive(orderRequest.getIsActive());
        }

        newOrder.setVersion((long) 1);

        orderRepository.save(newOrder);

        Map<Long, Product> products = (Map<Long, Product>) orderDependencies.get("products");
        Map<Long, Shop> shops = (Map<Long, Shop>) orderDependencies.get("shops");
        Map<Long, Unit> units = (Map<Long, Unit>) orderDependencies.get("units");

        orderRepository.save(newOrder);

        for (int i=0; i < orderRequest.getItems().size(); i++) {
            OrderItem newItem = new OrderItem();
            newOrder.getItems().add(newItem);
            OrderItemRequest requestItem = orderRequest.getItems().get(i);

            newItem.setOrderId(newOrder);
            newItem.setProduct(products.get(requestItem.getProduct_id()));
            newItem.setShop(shops.get(requestItem.getShop_id()));
            newItem.setUnit(units.get(requestItem.getUnit_id()));
            newItem.setQuantity(requestItem.getQuantity());
        }

        orderRepository.save(newOrder);

        return "";
    }

    private Map<String, Map<Long,?>> loadOrderDependencies(@RequestBody OrderRequest orderRequest, StringJoiner resultOfRequest){

        Set<Long> shopIDs = new LinkedHashSet<Long>();
        for (int i=0; i<orderRequest.getItems().size(); i++){
            shopIDs.add(orderRequest.getItems().get(i).getShop_id());
        }

        List<Shop> listOfShops = shopRepository.findAllById(shopIDs);
        if (listOfShops.size() != shopIDs.size()){
            resultOfRequest.add("Некорректный id магазина");
        }

        Map<Long, Shop> shopMap = new HashMap<Long, Shop>();
        for (int i=0; i<listOfShops.size(); i++){
            shopMap.put(listOfShops.get(i).getId(), listOfShops.get(i));
        }

        Set<Long> productIDs = new LinkedHashSet<Long>();
        for (int i=0; i<orderRequest.getItems().size(); i++){
            productIDs.add(orderRequest.getItems().get(i).getProduct_id());
        }

        List<Product> listOfProducts = productRepository.findAllById(productIDs);
        if (listOfProducts.size() != productIDs.size()){
            resultOfRequest.add("Некорректный id номенклатуры");
        }

        Map<Long, Product> productMap = new HashMap<Long, Product>();
        for (int i=0; i<listOfProducts.size(); i++){
            productMap.put(listOfProducts.get(i).getId(), listOfProducts.get(i));
        }

        Set<Long> unitIDs = new LinkedHashSet<Long>();
        for (int i=0; i<orderRequest.getItems().size(); i++){
            unitIDs.add(orderRequest.getItems().get(i).getUnit_id());
        }

        List<Unit> listOfUnits = unitRepository.findAllById(unitIDs);
        if (listOfUnits.size() != unitIDs.size()){
            resultOfRequest.add("Некорректный id единицы измерения");
        }

        Map<Long, Unit> unitMap = new HashMap<Long, Unit>();
        for (int i=0; i<listOfUnits.size(); i++){
            unitMap.put(listOfUnits.get(i).getId(), listOfUnits.get(i));
        }

        Map<String, Map<Long,?>> result = new HashMap<String, Map<Long,?>>();
        result.put("shops", shopMap);
        result.put("products", productMap);
        result.put("units", unitMap);

        return result;
    }
}
