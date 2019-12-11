package com.example.d.linking.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.d.linking.Adapter.DirAddAdapter2;
import com.example.d.linking.Data.DirectoryResponse;
import com.example.d.linking.R;
import com.example.d.linking.Server.APIClient;
import com.example.d.linking.Server.APIInterface;
import com.google.gson.Gson;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LinkSave_DirPopup extends Activity{
    private APIInterface service ;
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private String display_name;
    private Button btn_discard;
    private String URL;
    RecyclerView mRecyclerView, mRecyclerView2, mRecyclerView3;
    RecyclerView.LayoutManager mLayoutManager, mLayoutManager2, mLayoutManager3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀 바 삭제.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_linksave_dirpopup);
        preferences = getSharedPreferences("user",MODE_PRIVATE);
        display_name = preferences.getString("display_name","");

        Intent intent = getIntent();
        URL = intent.getStringExtra("URL");

        service = APIClient.getClient().create(APIInterface.class);

        btn_discard = findViewById(R.id.btn_discard2);
        btn_discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = preferences.edit();
                editor.putString("URL",URL);
                editor.commit();
                finish();
            }
        });

        mRecyclerView = findViewById(R.id.nav_recyclerPrivate);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView2 = findViewById(R.id.nav_recyclerPublic);
        mRecyclerView2.setHasFixedSize(true);
        mLayoutManager2 = new LinearLayoutManager(this);
        mRecyclerView2.setLayoutManager(mLayoutManager2);
        mRecyclerView3 = findViewById(R.id.nav_recyclerShared);
        mRecyclerView3.setHasFixedSize(true);
        mLayoutManager3 = new LinearLayoutManager(this);
        mRecyclerView3.setLayoutManager(mLayoutManager3);

        directoryList(display_name);
    }

    private void directoryList(String display_name) {
        Call<ArrayList<DirectoryResponse>> dirlist0 = service.dirList0(display_name);
        Call<ArrayList<DirectoryResponse>> dirlist1 = service.dirList1(display_name);
        Call<ArrayList<DirectoryResponse>> dirlist2 = service.dirList2(display_name);

        dirlist0.enqueue(new Callback<ArrayList<DirectoryResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<DirectoryResponse>> call, Response<ArrayList<DirectoryResponse>> response) {
                Log.d("통신성공"," "+new Gson().toJson(response.body()));
                DirAddAdapter2 dir_Adapter = new DirAddAdapter2(response.body());
                mRecyclerView.setAdapter(dir_Adapter);
            }
            @Override
            public void onFailure(Call<ArrayList<DirectoryResponse>> call, Throwable t) {
                Log.d("디렉토리 리스트 통신 실패","");
                t.printStackTrace();
            }
        });

        dirlist1.enqueue(new Callback<ArrayList<DirectoryResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<DirectoryResponse>> call, Response<ArrayList<DirectoryResponse>> response) {
                Log.d("통신성공"," "+new Gson().toJson(response.body()));
                DirAddAdapter2 dir_Adapter = new DirAddAdapter2(response.body());
                mRecyclerView2.setAdapter(dir_Adapter);
            }
            @Override
            public void onFailure(Call<ArrayList<DirectoryResponse>> call, Throwable t) {
                Log.d("디렉토리 리스트 통신 실패","");
                t.printStackTrace();
            }
        });

        dirlist2.enqueue(new Callback<ArrayList<DirectoryResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<DirectoryResponse>> call, Response<ArrayList<DirectoryResponse>> response) {
                Log.d("통신성공"," "+new Gson().toJson(response.body()));
                DirAddAdapter2 dir_Adapter = new DirAddAdapter2(response.body());
                mRecyclerView3.setAdapter(dir_Adapter);
            }
            @Override
            public void onFailure(Call<ArrayList<DirectoryResponse>> call, Throwable t) {
                Log.d("디렉토리 리스트 통신 실패","");
                t.printStackTrace();
            }
        });
    }

}