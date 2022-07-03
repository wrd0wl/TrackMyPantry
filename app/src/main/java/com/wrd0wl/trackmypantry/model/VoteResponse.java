package com.wrd0wl.trackmypantry.model;

import com.google.gson.annotations.SerializedName;

public class VoteResponse {
    @SerializedName("id")
    private String id;

    @SerializedName("rating")
    private int rating;

    @SerializedName("productId")
    private String productId;

    @SerializedName("userId")
    private String userId;

    @SerializedName("createdAt")
    private String created;

    @SerializedName("updatedAt")
    private String updated;

    public VoteResponse(String id, int rating, String productId, String userId, String created, String updated) {
        this.id = id;
        this.rating = rating;
        this.productId = productId;
        this.userId = userId;
        this.created = created;
        this.updated = updated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
}
