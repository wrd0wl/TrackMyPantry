package com.wrd0wl.trackmypantry.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class ItemData {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private String id;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    private String name;

    @ColumnInfo(name = "description")
    @SerializedName("description")
    private String description;

    @ColumnInfo(name = "barcode")
    @SerializedName("barcode")
    private String barcode;

    @Ignore
    @SerializedName("userId")
    private String userId;

    @Ignore
    @SerializedName("test")
    private boolean test;

    @Ignore
    @SerializedName("createdAt")
    private String created;

    @Ignore
    @SerializedName("updatedAt")
    private String updated;

    @ColumnInfo(name = "quantity")
    private int quantity;

    @ColumnInfo(name = "isRemote")
    private boolean isRemote;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "expiry_date")
    private String expiryDate;

    @ColumnInfo(name = "email")
    private String email;


    public ItemData(@NonNull String id, String name, String description, String barcode, int quantity, String category, String expiryDate, boolean isRemote, String email) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.barcode = barcode;
        this.quantity = quantity;
        this.category = category;
        this.expiryDate = expiryDate;
        this.isRemote = isRemote;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getCategory() {
        return category;
    }

    public void setCategoryImageId(String category) {
        this.category = category;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isRemote() {
        return isRemote;
    }

    public void setRemote(boolean remote) {
        isRemote = remote;
    }
}
