package com.example.d.linking.Server;

import com.example.d.linking.Data.LoginData;
import com.example.d.linking.Data.LoginResponse;
import com.example.d.linking.Data.DirectoryResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIInterface {

    @POST("user")
    Call<LoginResponse> userLogin(@Body LoginData data);

    @POST("directory/{display_name}")
    Call<ArrayList<DirectoryResponse>> dirList(@Path("display_name") String display_name);

    //@FormUrlEncoded

}