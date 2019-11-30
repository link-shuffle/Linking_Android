package com.example.d.linking.Data;

import com.google.gson.annotations.SerializedName;

public class LinkEditData {

    @SerializedName("tag")
    String tag;

    @SerializedName("desc")
    String desc;

    public LinkEditData(String tag, String desc) {
        this.tag = tag;
        this.desc = desc;
    }
}
