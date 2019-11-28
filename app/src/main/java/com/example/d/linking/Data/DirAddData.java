package com.example.d.linking.Data;

import com.google.gson.annotations.SerializedName;

public class DirAddData {
    @SerializedName("name")
    String name;

    public DirAddData(String name) {
        this.name = name;
    }
}
