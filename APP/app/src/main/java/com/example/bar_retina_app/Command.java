package com.example.bar_retina_app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Command {
    private int id;
    private ArrayList<Product> products;
    private int tableId;
    private String bartender;
    private int totalPrice;

    public Command(int id, int tableId, String bartender) {
        this.id = id;
        this.tableId = tableId;
        this.bartender = bartender;
        this.totalPrice = 0;
        this.products = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product){
        this.products.add(product);
    }

    public int getTableId() {
        return tableId;
    }

    public void setTable(int tableId) {
        this.tableId = tableId;
    }

    public String getBartender() {
        return bartender;
    }

    public void setBartender(String bartender) {
        this.bartender = bartender;
    }

    public List<CommandProduct> getGroupedProducts() {
        HashMap<String, CommandProduct> groupedMap = new HashMap<>();

        for (Product product : products) {
            String productName = product.getName();
            CommandProduct groupedProduct = groupedMap.getOrDefault(productName, new CommandProduct(product, 0));
            groupedProduct.setQuantity(groupedProduct.getQuantity() + 1);
            groupedMap.put(productName, groupedProduct);
        }

        return new ArrayList<>(groupedMap.values());
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
