package com.example.d.linking.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

public class Directory_Add2 extends AppCompatActivity {
    private APIInterface service;
    private SharedPreferences preferences;
    FloatingActionButton btn_dir_add;
    //디렉토리 list recycler
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    String display_name;
    int dir_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory_add);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        dir_id = intent.getIntExtra("dirID",0);

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

        DirList();

        //link 추가 팝업 버튼
        btn_dir_add = (FloatingActionButton) findViewById(R.id.dir_add);
        btn_dir_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Directory_Add2.this, DirSave_Popup.class);
                intent.putExtra("dirID",dir_id);
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
            case R.id.btn_diradd:{
                Intent intent = new Intent(Directory_Add2.this, DirSave_Popup.class);
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    public void DirList(){
        Call<ArrayList<DirectoryResponse>> dirlist = service.dirListSub(display_name, dir_id);
        dirlist.enqueue(new Callback<ArrayList<DirectoryResponse>>() {
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
    }
}
