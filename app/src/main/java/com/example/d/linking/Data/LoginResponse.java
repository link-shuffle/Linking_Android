package com.example.d.linking.Data;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("code")
    private int code;

    public int getCode() {
        return code;
    }
}
