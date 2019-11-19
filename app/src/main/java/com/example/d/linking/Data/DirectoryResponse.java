package com.example.d.linking.Data;

import com.google.gson.annotations.SerializedName;

public class DirectoryResponse
{

private String dir_id;
private String _id;
private String name;

        public String getDir_id() {
            return dir_id;
        }

        public void setDir_id(String dir_id) {
            this.dir_id = dir_id;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }



/*
    @SerializedName("dir_id")
    private int dir_id;

    @SerializedName("name")
    private int name;

    public int getDir_id() {
        return dir_id;
    }

    public int getName() {
        return name;
    }
   */
}
