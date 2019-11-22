package com.example.d.linking.Data;

import com.google.gson.annotations.SerializedName;

public class LinkAddResponse {
    @SerializedName("meta_title")
    private String meta_title;

    @SerializedName("meta_desc")
    private String meta_desc;

    @SerializedName("meta_imgUrl")
    private String meta_imgUrl;

    public String getMeta_title() {
        return meta_title;
    }

    public String getMeta_desc() {
        return meta_desc;
    }

    public String getMeta_imgUrl() {
        return meta_imgUrl;
    }
}
