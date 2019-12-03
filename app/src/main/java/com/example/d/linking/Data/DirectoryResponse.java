package com.example.d.linking.Data;

import com.google.gson.annotations.SerializedName;

public class DirectoryResponse {

    @SerializedName("dir_id")
    private int dir_id;

    @SerializedName("name")
    private String name;

    @SerializedName("dir_type")
    private int dir_type;

        public int getDir_id() {
            return dir_id;
        }

        public void setDir_id(int dir_id) {
            this.dir_id = dir_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getDir_type() { return dir_type;}
}
