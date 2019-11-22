package com.example.d.linking.Data;

import com.google.gson.annotations.SerializedName;

public class LinkAddData {

    @SerializedName("link")
    String link;

    @SerializedName("tag")
    String tag;

    @SerializedName("desc")
    String desc;

    public LinkAddData(String link, String tag, String desc) {
        this.link = link;
        this.tag = tag;
        this.desc = desc;
    }
}
