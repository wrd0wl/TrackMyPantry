package com.wrd0wl.trackmypantry.model;

public class RegisterData extends  LoginData{

    private String username;

    public RegisterData(String username, String email, String password) {
        super(email, password);
        this.username = username;
    }
}
