package com.example.d.linking.Data;

import com.google.gson.annotations.SerializedName;

public class SearchUserResponse {
    @SerializedName("name")
    private String name;

    @SerializedName("display_name")
    private String display_name;

    public String getName() {
        return name;
    }

    public String getDisplay_name() {
        return display_name;
    }

}
