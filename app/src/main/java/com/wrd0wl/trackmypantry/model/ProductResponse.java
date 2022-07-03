package com.wrd0wl.trackmypantry.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductResponse {
    @SerializedName("products")
    private List<ItemData> products;

    @SerializedName("token")
    private String token;

    public ProductResponse(List<ItemData> products, String token) {
        this.products = products;
        this.token = token;
    }

    public List<ItemData> getProducts() {
        return products;
    }

    public void setProducts(List<ItemData> products) {
        this.products = products;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
