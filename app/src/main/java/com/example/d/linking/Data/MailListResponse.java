package com.example.d.linking.Data;

import com.google.gson.annotations.SerializedName;

public class MailListResponse {

    @SerializedName("sender")
    private String sender;

    @SerializedName("message")
    private String message;

    @SerializedName("display_name")
    private String display_name;

    @SerializedName("status")
    private int status;

    @SerializedName("mail_id")
    private int mail_id;

    @SerializedName("dir_id")
    private int dir_id;

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public String getDisplay_name() {return display_name;}

    public int getStatus() {return status;}

    public int getMail_id() {return mail_id;}

    public int getDir_id() { return dir_id; }
}
