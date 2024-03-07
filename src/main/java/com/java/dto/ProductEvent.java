package com.java.dto;

import com.java.entity.Product;

public class ProductEvent {

    private String eventName;

    private Product product;

    public ProductEvent() {
    }

    public ProductEvent(String eventName, Product product) {
        this.eventName = eventName;
        this.product = product;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
