package com.example.d.linking.Server;

import com.example.d.linking.Data.LoginResponse;
import com.example.d.linking.Data.DirectoryResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIInterface {

    @POST("user")
    Call<LoginResponse> userLogin(@Body String data);

    @POST("directory/{display_name}")
    Call<DirectoryResponse> dirList(@Path("display_name") String display_name);

    //@FormUrlEncoded

}