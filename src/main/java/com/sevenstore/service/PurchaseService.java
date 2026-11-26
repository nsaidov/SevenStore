package com.sevenstore.service;

import com.sevenstore.entity.*;
import com.sevenstore.repository.ProductRepository;
import com.sevenstore.repository.PurchaseRepository;
import com.sevenstore.repository.UnitRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.*;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final ProductRepository productRepository;
    private final UnitRepository unitRepository;

    public PurchaseService(PurchaseRepository purchaseRepository, ProductRepository productRepository, UnitRepository unitRepository){
        this.purchaseRepository = purchaseRepository;
        this.productRepository = productRepository;
        this.unitRepository = unitRepository;
    }

    public void createPurchase(Order order){
        Purchase newPurchase = new Purchase();
        newPurchase.setOrderId(order);
        newPurchase.setVersion((long) 0);

        fillItemsFromOrder(newPurchase, order);

        purchaseRepository.save(newPurchase);
    }

    @Transactional
    public String editPurchase(@RequestBody PurchaseRequest purchaseRequest) {

        StringJoiner resultOfValidation = new StringJoiner("; ");

        Purchase purchase = purchaseRepository.findById(purchaseRequest.getPurchaseId())
                .orElse(null);

        if (purchase == null) {
            return resultOfValidation.add("ID закупки введен неверно").toString();
        }

        purchase.setVersion(purchase.getVersion()+1);

        purchase.getItems().clear();

        purchaseRepository.flush();

        Map<String, Map<Long, ?>> purchaseDependencies = loadOrderDependencies(purchaseRequest, resultOfValidation);

        fillListOfProducts(purchaseRequest, purchase, purchaseDependencies);

        if (resultOfValidation.length() > 0) {
            return resultOfValidation.toString();
        }

        fillItemsFromOrder(purchase, purchase.getOrderId());

        purchaseRepository.save(purchase);

        return "";
    }

    private static void fillItemsFromOrder(Purchase purchase, Order order){

        Set<Product> productsFromOrder = new HashSet<>();

        for (OrderItem orderItem: order.getItems()) {
            productsFromOrder.add(orderItem.getProduct());
        }

        for (Product product: productsFromOrder) {
            PurchaseItem purchaseItem = purchase.getItems().stream().filter(i -> i.getProduct().equals(product)).findFirst()
                    .orElse(null);

            if (product == null) {
                purchaseItem = new PurchaseItem();
                purchase.getItems().add(purchaseItem);
                purchaseItem.setProduct(product);
                purchaseItem.setPurchaseId(purchase);
                purchaseItem.setPrice((long) 0);
                purchaseItem.setUnit(product.getUnitPurchase());
                purchaseItem.setQuantity(BigDecimal.valueOf(0));
            }
        }
    }

    private Map<String, Map<Long,?>> loadOrderDependencies(@RequestBody PurchaseRequest purchaseRequest, StringJoiner resultOfRequest){

        Set<Long> productIDs = new LinkedHashSet<>();
        for (PurchaseItemRequest purchaseItemRequest : purchaseRequest.getItems()) {
            productIDs.add(purchaseItemRequest.getProduct_id());
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
        for (PurchaseItemRequest purchaseItemRequest : purchaseRequest.getItems()) {
            unitIDs.add(purchaseItemRequest.getUnit_id());
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
        result.put("products", productMap);
        result.put("units", unitMap);

        return result;
    }

    private void fillListOfProducts(@RequestBody PurchaseRequest purchaseRequest, Purchase purchase, Map<String, Map<Long, ?>> purchaseDependencies) {
        Map<Long, Product> products = (Map<Long, Product>) purchaseDependencies.get("products");
        Map<Long, Unit> units = (Map<Long, Unit>) purchaseDependencies.get("units");

        for (int i=0; i < purchaseRequest.getItems().size(); i++) {
            PurchaseItem newItem = new PurchaseItem();
            purchase.getItems().add(newItem);
            PurchaseItemRequest requestItem = purchaseRequest.getItems().get(i);

            newItem.setPurchaseId(purchase);
            newItem.setProduct(products.get(requestItem.getProduct_id()));
            newItem.setUnit(units.get(requestItem.getUnit_id()));
            newItem.setQuantity(requestItem.getQuantity());
            newItem.setPrice(requestItem.getPrice());
        }

    }
}


