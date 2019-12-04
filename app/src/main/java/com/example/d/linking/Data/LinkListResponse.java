package com.example.d.linking.Data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

public class LinkListResponse {
    @SerializedName("link")
    private String link;

    @SerializedName("link_id")
    private int link_id;

    @SerializedName("tag")
    private ArrayList<String> link_tag;

    @SerializedName("desc")
    private String desc;

    @SerializedName("meta_title")
    private String  meta_title;

    @SerializedName("meta_desc")
    private String  meta_desc;

    @SerializedName("meta_imgUrl")
    private String  meta_imgUrl;

    @SerializedName("read_status")
    private int  read_status;

    @SerializedName("favorite_status")
    private int  favorite_status;

    @SerializedName("created_time")
    private Date created_time;

    @SerializedName("revised_time")
    private Date  revised_time;

    public String getLink() {
        return link;
    }

    public ArrayList<String> getLink_tag() {
        return link_tag;
    }

    public String getDesc() {
        return desc;
    }

    public String getMeta_title() {
        return meta_title;
    }

    public String getMeta_desc() {
        return meta_desc;
    }

    public String getMeta_imgUrl() {
        return meta_imgUrl;
    }

    public int getRead_status() {
        return read_status;
    }

    public int getFavorite_status() {return  favorite_status;}

    public int getLink_id(){return link_id;}

    public Date getCreate_time() {
        return created_time;
    }

    public Date getRevised_time() {
        return revised_time;
    }
}
