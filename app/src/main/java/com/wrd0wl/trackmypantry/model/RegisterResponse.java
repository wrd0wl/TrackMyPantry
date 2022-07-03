package com.wrd0wl.trackmypantry.model;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse {
    @SerializedName("id")
    private String id;
    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("updatedAt")
    private String updatedAt;

    public RegisterResponse(String id, String username, String email, String password, String createdAt, String updatedAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
