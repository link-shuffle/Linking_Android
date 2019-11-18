package com.example.d.linking.Server;

import com.example.d.linking.Data.LoginResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {
    @POST("/name")
    Call<LoginResponse> userLogin(@Query("email") String email);
}