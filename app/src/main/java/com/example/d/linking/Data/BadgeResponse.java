package com.example.d.linking.Data;

import com.google.gson.annotations.SerializedName;

public class BadgeResponse {
    @SerializedName("mailnumber")
    private String mailnumber;

    public String getMailnumber() {
        return mailnumber;
    }
}
