package com.example.d.linking.Data;


import com.google.gson.annotations.SerializedName;

public class LoginData {
    @SerializedName("email")
    String email;

    @SerializedName("name")
    String name;

    public LoginData(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
