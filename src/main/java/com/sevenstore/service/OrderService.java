package com.sevenstore.service;

import com.sevenstore.entity.*;
import com.sevenstore.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final PurchaseService purchaseService;


    public OrderService(ShopRepository shopRepository, ProductRepository productRepository, UnitRepository unitRepository, OrderRepository orderRepository, PurchaseService purchaseService){
        this.shopRepository = shopRepository;
        this.productRepository = productRepository;
        this.unitRepository = unitRepository;
        this.orderRepository = orderRepository;
        this.purchaseService = purchaseService;
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

        fillListOfProducts(orderRequest, newOrder, orderDependencies);

        orderRepository.save(newOrder);

        purchaseService.createPurchase(newOrder);

        return "";
    }

    private Map<String, Map<Long,?>> loadOrderDependencies(@RequestBody OrderRequest orderRequest, StringJoiner resultOfRequest){

        Set<Long> shopIDs = new LinkedHashSet<>();
        for (OrderItemRequest orderItemRequest : orderRequest.getItems()) {
            shopIDs.add(orderItemRequest.getShop_id());
        }

        List<Shop> listOfShops = shopRepository.findAllById(shopIDs);
        if (listOfShops.size() != shopIDs.size()){
            resultOfRequest.add("Некорректный id магазина");
        }

        Map<Long, Shop> shopMap = new HashMap<>();
        for (Shop shop : listOfShops) {
            shopMap.put(shop.getId(), shop);
        }

        Set<Long> productIDs = new LinkedHashSet<>();
        for (OrderItemRequest orderItemRequest : orderRequest.getItems()) {
            productIDs.add(orderItemRequest.getProduct_id());
        }

        List<Product> listOfProducts = productRepository.findAllById(productIDs);
        if (listOfProducts.size() != productIDs.size()){
            resultOfRequest.add("Некорректный id номенклатуры");
        }

        Map<Long, Product> productMap = new HashMap<>();
        for (Product product : listOfProducts){
            productMap.put(product.getId(), product);
        }

        Set<Long> unitIDs = new LinkedHashSet<>();
        for (OrderItemRequest orderItemRequest : orderRequest.getItems()) {
            unitIDs.add(orderItemRequest.getUnit_id());
        }

        List<Unit> listOfUnits = unitRepository.findAllById(unitIDs);
        if (listOfUnits.size() != unitIDs.size()){
            resultOfRequest.add("Некорректный id единицы измерения");
        }

        Map<Long, Unit> unitMap = new HashMap<>();
        for (Unit unit : listOfUnits) {
            unitMap.put(unit.getId(), unit);
        }

        Map<String, Map<Long,?>> result = new HashMap<>();
        result.put("shops", shopMap);
        result.put("products", productMap);
        result.put("units", unitMap);

        return result;
    }

    private void fillListOfProducts(OrderRequest orderRequest, Order order, Map<String, Map<Long, ?>> orderDependencies) {
        Map<Long, Product> products = (Map<Long, Product>) orderDependencies.get("products");
        Map<Long, Shop> shops = (Map<Long, Shop>) orderDependencies.get("shops");
        Map<Long, Unit> units = (Map<Long, Unit>) orderDependencies.get("units");

        for (int i=0; i < orderRequest.getItems().size(); i++) {
            OrderItem newItem = new OrderItem();
            order.getItems().add(newItem);
            OrderItemRequest requestItem = orderRequest.getItems().get(i);

            newItem.setOrderId(order);
            newItem.setProduct(products.get(requestItem.getProduct_id()));
            newItem.setShop(shops.get(requestItem.getShop_id()));
            newItem.setUnit(units.get(requestItem.getUnit_id()));
            newItem.setQuantity(requestItem.getQuantity());
        }
    }

    @Transactional
    public Order getOrder(@PathVariable Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Transactional
    public String editOrder(@RequestBody OrderRequest orderRequest) {

        StringJoiner resultOfValidation = new StringJoiner("; ");

        Order order = orderRepository.findById(orderRequest.getOrderId()).orElse(null);

        if (order == null) {
            resultOfValidation.add("Заказ с таким id не обнаружен");
            return resultOfValidation.toString();
        }

        Map<String, Map<Long,?>> orderDependencies = loadOrderDependencies(orderRequest, resultOfValidation);

        if (resultOfValidation.length() > 0) {
            return resultOfValidation.toString();
        }

        order.getItems().clear();
        orderRepository.flush();
        fillListOfProducts(orderRequest, order, orderDependencies);

        if (orderRequest.getName() != null){
            order.setName(orderRequest.getName());
        }

        if (orderRequest.getIsActive() != null) {
            order.setIsActive(orderRequest.getIsActive());
        }

        if (orderRequest.getCreatedAt() != null) {
            order.setCreatedAt(orderRequest.getCreatedAt());
        }

        order.setVersion(order.getVersion()+1);

        orderRepository.save(order);

        return "";
    }
}
