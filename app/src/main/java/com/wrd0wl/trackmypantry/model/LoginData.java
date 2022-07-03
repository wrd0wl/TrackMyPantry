package com.wrd0wl.trackmypantry.model;

public class LoginData {
    private String email;
    private String password;

    public LoginData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
}
