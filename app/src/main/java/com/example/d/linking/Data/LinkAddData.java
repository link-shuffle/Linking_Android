package com.example.d.linking.Data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LinkAddData {

    @SerializedName("link")
    String link;

    @SerializedName("tag")
    ArrayList<String> tag;

    @SerializedName("desc")
    String desc;

    public LinkAddData(String link, ArrayList<String> tag, String desc) {
        this.link = link;
        this.tag = tag;
        this.desc = desc;
    }
}
