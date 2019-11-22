package com.example.d.linking.Server;

import com.example.d.linking.Data.LinkAddData;
import com.example.d.linking.Data.LinkAddResponse;
import com.example.d.linking.Data.LinkListResponse;
import com.example.d.linking.Data.LoginData;
import com.example.d.linking.Data.LoginResponse;
import com.example.d.linking.Data.DirectoryResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIInterface {

    @POST("user")
    Call<LoginResponse> userLogin(@Body LoginData data);

    @POST("directory/{display_name}")
    Call<ArrayList<DirectoryResponse>> dirList(@Path("display_name") String display_name);

    @POST("directory/{display_name}/{dir_id}")
    Call<ArrayList<DirectoryResponse>> dirListSub(@Path("display_name") String display_name, @Path("dir_id") int dir_id);

    @POST("link/{dir_id}/saved")
    Call<LinkAddResponse> linkadd(@Path("dir_id") int dir_id, @Body LinkAddData data);

    @POST("link/{dir_id}")
    Call<ArrayList<LinkListResponse>> linklist(@Path("dir_id") int dir_id);

    //@FormUrlEncoded

}