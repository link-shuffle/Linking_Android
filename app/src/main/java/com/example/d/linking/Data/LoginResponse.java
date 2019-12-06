package com.example.d.linking.Data;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("display_name")
    private String display_name;

    public int getCode() {
        return code;
    }

    public String getDisplay_name() {return display_name;}
}
