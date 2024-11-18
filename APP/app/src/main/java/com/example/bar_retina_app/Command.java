package com.example.bar_retina_app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Command {
    private int id;
    private ArrayList<Product> products;
    private String table;
    private String bartender;
    private int totalPrice;

    public Command(int id, String table, String bartender) {
        this.id = id;
        this.table = table;
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

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
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
