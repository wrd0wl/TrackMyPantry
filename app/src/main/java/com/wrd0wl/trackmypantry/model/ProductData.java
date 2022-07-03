package com.wrd0wl.trackmypantry.model;

public class ProductData {
    private String token;
    private String name;
    private String description;
    private String barcode;
    private boolean test;

    public ProductData(String token, String name, String description, String barcode, boolean test) {
        this.token = token;
        this.name = name;
        this.description = description;
        this.barcode = barcode;
        this.test = test;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }
}
