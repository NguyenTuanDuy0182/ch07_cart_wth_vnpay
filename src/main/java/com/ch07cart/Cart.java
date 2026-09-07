package com.ch07cart;

import java.io.Serializable;
import java.util.ArrayList;

public class Cart implements Serializable {

    private ArrayList<LineItem> items;

    public Cart() {
        items = new ArrayList<LineItem>();
    }

    public ArrayList<LineItem> getItems() {
        return items;
    }

    public int getCount() {
        return items.size();
    }

    public void addItem(LineItem item) {
        if (item == null || item.getProduct() == null) {
            return;
        }
        String code = item.getProduct().getCode();
        int quantity = item.getQuantity();
        for (LineItem cartItem : items) {
            if (cartItem.getProduct() != null && cartItem.getProduct().getCode().equals(code)) {
                cartItem.setQuantity(quantity);
                return;
            }
        }
        items.add(item);
    }

    public void removeItem(LineItem item) {
        if (item == null || item.getProduct() == null) {
            return;
        }
        String code = item.getProduct().getCode();
        for (int i = 0; i < items.size(); i++) {
            LineItem lineItem = items.get(i);
            if (lineItem.getProduct() != null && lineItem.getProduct().getCode().equals(code)) {
                items.remove(i);
                return;
            }
        }
    }
}