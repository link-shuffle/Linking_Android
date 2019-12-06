package com.example.d.linking.Data;

import com.google.gson.annotations.SerializedName;

public class MailData {

    @SerializedName("mail_id")
    int mail_id;

    @SerializedName("dir_id")
    int dir_id;

    public MailData(int mail_id,int dir_id) {
        this.mail_id = mail_id;
        this.dir_id = dir_id;
    }
}
