package com.example.d.linking.Data;

import com.google.gson.annotations.SerializedName;

public class DirectoryResponse {

    private int dir_id;
    private String name;

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

}
