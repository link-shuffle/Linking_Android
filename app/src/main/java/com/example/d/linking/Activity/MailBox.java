package com.example.d.linking.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;

import com.example.d.linking.Adapter.MailListAdapter;
import com.example.d.linking.Data.MailListResponse;
import com.example.d.linking.R;
import com.example.d.linking.Server.APIClient;
import com.example.d.linking.Server.APIInterface;
import com.google.gson.Gson;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MailBox extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private APIInterface service;
    private SharedPreferences preferences;
    private String display_name;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mailbox);
        preferences = getSharedPreferences("user", MODE_PRIVATE);
        display_name = preferences.getString("display_name","");

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setTitle(Html.fromHtml("<font color='#2c3130'>"+display_name+"</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        service= APIClient.getClient().create(APIInterface.class);
        mRecyclerView = (RecyclerView) findViewById(R.id.mail_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mail(display_name);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int swipedPosition = viewHolder.getAdapterPosition();
                MailListAdapter adapter = (MailListAdapter) mRecyclerView.getAdapter();
                adapter.remove(swipedPosition);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            Intent intent = new Intent(MailBox.this,Workspace.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }

    public void mail(String display_name){
        Call<ArrayList<MailListResponse>> mailList = service.maillist(display_name);
        mailList.enqueue(new Callback<ArrayList<MailListResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<MailListResponse>> call, Response<ArrayList<MailListResponse>> response) {
                Log.d("통신성공"," "+new Gson().toJson(response.body()));
                MailListAdapter adapter = new MailListAdapter(response.body());
                mRecyclerView.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<ArrayList<MailListResponse>> call, Throwable t) {
                Log.d("메일 리스트 통신 실패","");
                t.printStackTrace();
            }
        });
    }
}
