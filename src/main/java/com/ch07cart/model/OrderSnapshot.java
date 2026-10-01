package com.ch07cart.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderSnapshot implements Serializable {
    private String txnRef;
    private User user;
    private double total;
    private List<LineItem> items;

    public OrderSnapshot(String txnRef, User user, double total, List<LineItem> cartItems) {
        this.txnRef = txnRef;
        this.user = new User(user.getUsername(), user.getEmail());
        this.total = total;
        this.items = new ArrayList<>();
        if (cartItems != null) {
            for (LineItem ci : cartItems) {
                Product p = new Product();
                p.setCode(ci.getProduct().getCode());
                p.setDescription(ci.getProduct().getDescription());
                p.setPrice(ci.getProduct().getPrice());

                LineItem item = new LineItem();
                item.setProduct(p);
                item.setQuantity(ci.getQuantity());
                this.items.add(item);
            }
        }
    }

    public String getTxnRef() {
        return txnRef;
    }

    public User getUser() {
        return user;
    }

    public double getTotal() {
        return total;
    }

    public List<LineItem> getItems() {
        return items;
    }
}
