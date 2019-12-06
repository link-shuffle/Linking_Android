package com.example.d.linking.Data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LinkEditData {

    @SerializedName("tag")
    ArrayList<String> tag;

    @SerializedName("desc")
    String desc;

    public LinkEditData(ArrayList<String> tag, String desc) {
        this.tag = tag;
        this.desc = desc;
    }
}
