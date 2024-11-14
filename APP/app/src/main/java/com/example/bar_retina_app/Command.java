package com.example.bar_retina_app;

import java.util.ArrayList;

public class Command {
    private int id;
    private ArrayList<Product> products;
    private String table;
    private String bartender;
    private String state;
    private int totalPrice;

    public Command(int id, String table, String bartender) {
        this.id = id;
        this.table = table;
        this.bartender = bartender;
        this.totalPrice = 0;
        this.state = state;
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
}
