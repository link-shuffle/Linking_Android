package com.example.d.linking.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.d.linking.Adapter.DirAddAdapter;
import com.example.d.linking.Adapter.DirListAdapter;
import com.example.d.linking.Data.DirectoryResponse;
import com.example.d.linking.R;
import com.example.d.linking.Server.APIClient;
import com.example.d.linking.Server.APIInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Directory_Add extends AppCompatActivity {
    private APIInterface service;
    private SharedPreferences preferences;
    Button btn_diradd2;
    RecyclerView mRecyclerView, mRecyclerView2, mRecyclerView3;
    RecyclerView.LayoutManager mLayoutManager, mLayoutManager2, mLayoutManager3;
    String display_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory_add);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#2c3130'>Linking</font>"));

        //server connection
        service= APIClient.getClient().create(APIInterface.class);

        //display_name 가져오기.
        preferences = getSharedPreferences("user", MODE_PRIVATE);
        display_name = preferences.getString("display_name","");

        //디렉토리 list recycler
        mRecyclerView = findViewById(R.id.dir_private);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView2 = findViewById(R.id.dir_public);
        mRecyclerView2.setHasFixedSize(true);
        mLayoutManager2 = new LinearLayoutManager(this);
        mRecyclerView2.setLayoutManager(mLayoutManager2);
        mRecyclerView3 = findViewById(R.id.dir_shared);
        mRecyclerView3.setHasFixedSize(true);
        mLayoutManager3 = new LinearLayoutManager(this);
        mRecyclerView3.setLayoutManager(mLayoutManager3);

        DirList(display_name);

        btn_diradd2 = (Button) findViewById(R.id.btn_diradd2);
        btn_diradd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DirSave_Popup.class);
                intent.putExtra("dirID",0);
                startActivity(intent);
            }
        });

    }

    //toolbar 뒤로가기 버튼
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void DirList(String display_name){
        Call<ArrayList<DirectoryResponse>> dirlist0 = service.dirList0(display_name);
        Call<ArrayList<DirectoryResponse>> dirlist1 = service.dirList1(display_name);
        Call<ArrayList<DirectoryResponse>> dirlist2 = service.dirList2(display_name);
        dirlist0.enqueue(new Callback<ArrayList<DirectoryResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<DirectoryResponse>> call, Response<ArrayList<DirectoryResponse>> response) {
                Log.d("통신성공"," "+new Gson().toJson(response.body()));
                DirAddAdapter listAddAdapter = new DirAddAdapter(response.body());
                mRecyclerView.setAdapter(listAddAdapter);
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
                DirAddAdapter listAddAdapter = new DirAddAdapter(response.body());
                mRecyclerView2.setAdapter(listAddAdapter);
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
                DirAddAdapter listAddAdapter = new DirAddAdapter(response.body());
                mRecyclerView3.setAdapter(listAddAdapter);
            }
            @Override
            public void onFailure(Call<ArrayList<DirectoryResponse>> call, Throwable t) {
                Log.d("디렉토리 리스트 통신 실패","");
                t.printStackTrace();
            }
        });
    }
}
