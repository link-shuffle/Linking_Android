package com.example.d.linking.Data;

import com.google.gson.annotations.SerializedName;

public class FollowerResponse {

    @SerializedName("display_name")
    private String display_name;

    @SerializedName("name")
    private String name;

    public String getDisplay_name() {
        return display_name;
    }

    public String getName(){
        return name;
    }
}
