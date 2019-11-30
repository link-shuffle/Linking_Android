package com.example.d.linking.Server;

import com.example.d.linking.Data.DirAddData;
import com.example.d.linking.Data.FollowerResponse;
import com.example.d.linking.Data.LinkAddData;
import com.example.d.linking.Data.LinkAddResponse;
import com.example.d.linking.Data.LinkEditData;
import com.example.d.linking.Data.LinkListResponse;
import com.example.d.linking.Data.LoginData;
import com.example.d.linking.Data.LoginResponse;
import com.example.d.linking.Data.DirectoryResponse;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIInterface {

    @POST("user")
    Call<LoginResponse> userLogin(@Body LoginData data);

    @POST("directory/{display_name}/private")
    Call<ArrayList<DirectoryResponse>> dirList0(@Path("display_name") String display_name);

    @POST("directory/{display_name}/public")
    Call<ArrayList<DirectoryResponse>> dirList1(@Path("display_name") String display_name);

    @POST("directory/{display_name}/show/shared")
    Call<ArrayList<DirectoryResponse>> dirList2(@Path("display_name") String display_name);

    @POST("directory/{display_name}/{dir_id}")
    Call<ArrayList<DirectoryResponse>> dirListSub(@Path("display_name") String display_name, @Path("dir_id") int dir_id);

    @POST("link/{dir_id}/saved")
    Call<LinkAddResponse> linkadd(@Path("dir_id") int dir_id, @Body LinkAddData data);

    @POST("link/{dir_id}/read")
    Call<ArrayList<LinkListResponse>> linklist(@Path("dir_id") int dir_id);

    @GET("link/{link_id}/delete")
    Call<ResponseBody> linkdelete(@Path("link_id") int link_id);

    @POST("directory/{display_name}/{dir_id}/add")
    Call<ResponseBody> diradd(@Path("display_name") String display_name, @Path("dir_id") int dir_id, @Body DirAddData data);

    @GET("directory/{display_name}/{dir_id}/delete")
    Call<ResponseBody> dirdelete(@Path("display_name") String display_name, @Path("dir_id") int dir_id);

    @POST("directory/{display_name}/{dir_id}/update")
    Call<ResponseBody> dirrename(@Path("display_name") String display_name, @Path("dir_id") int dir_id, @Body DirAddData data);

    @POST("link/{link_id}/readState")
    Call<ResponseBody> linkstate(@Path("link_id") int link_id);

    @POST("link/{display_name}/favorite/call")
    Call<ArrayList<LinkListResponse>> linkfavorite(@Path("display_name") String display_name);

    @POST("link/{link_id}/update")
    Call<ResponseBody> linkupdate(@Path("link_id") int link_id, @Body LinkEditData data);

    @POST("follower/{display_name}/read")
    Call<ArrayList<FollowerResponse>> followerlist(@Path("display_name") String display_name);

    @POST("following/{display_name}/read")
    Call<ArrayList<FollowerResponse>> followinglist(@Path("display_name") String display_name);

    @GET("following/{display_name}/{following}/delete")
    Call<ResponseBody> followingdelete(@Path("display_name") String display_name, @Path("following") String following);

    @GET("user/{display_name}/delete")
    Call<ResponseBody> userdelete(@Path("display_name") String display_name);

    //@FormUrlEncoded

}