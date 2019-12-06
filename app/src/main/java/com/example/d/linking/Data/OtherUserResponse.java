package com.example.d.linking.Data;

import com.google.gson.annotations.SerializedName;

public class OtherUserResponse {
    @SerializedName("name")
    private String name;

    @SerializedName("followerNum")
    private String followerNum;

    @SerializedName("followingNum")
    private String followingNum;

    public String getName() {
        return name;
    }

    public String getFollowerNum() { return followerNum; }

    public String getFollowingNum() { return followingNum; }
}
