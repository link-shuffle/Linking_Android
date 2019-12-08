package com.example.d.linking.Data;

import com.google.gson.annotations.SerializedName;

public class UserData {
    @SerializedName("display_name")
    String display_name;

    public UserData(String display_name) {
        this.display_name = display_name;
    }
}
