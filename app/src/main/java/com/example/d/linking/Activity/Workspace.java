package com.example.d.linking.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.d.linking.Adapter.DirListAdapter;
import com.example.d.linking.Data.DirectoryResponse;
import com.example.d.linking.R;
import com.example.d.linking.Server.APIClient;
import com.example.d.linking.Server.APIInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Workspace extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private SharedPreferences preferences;
    Fragment workspace;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    FloatingActionButton btn_link_add;
    Toolbar toolbar;
    String display_name;
    Button user_id;
    //디렉토리 list recycler
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private APIInterface service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace);
        //display_name 가져오기.
        preferences = getSharedPreferences("user", MODE_PRIVATE);
        display_name = preferences.getString("display_name","");

        //server connection
        service= APIClient.getClient().create(APIInterface.class);

        initLayout();

        //fragment 화면 설정.
        workspace = new Fragment_workspace();
        getSupportFragmentManager().beginTransaction().add(R.id.container, workspace).commit();

        directoryList(display_name);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    private void initLayout() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Linking");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.dl_main_drawer_root);
        navigationView = (NavigationView) findViewById(R.id.nv_main_navigation_root);
        drawerToggle = new ActionBarDrawerToggle(Workspace.this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        navigationView.setNavigationItemSelectedListener(this);

        // xml 파일에서 넣어놨던 header 선언
        View header = navigationView.getHeaderView(0);
        // header에 있는 리소스 가져오기
        user_id = (Button) header.findViewById(R.id.user_id);
        user_id.setText(display_name);

        //디렉토리 list recycler
        mRecyclerView = findViewById(R.id.nav_recyclerPrivate);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //user setting 버튼
    public void user_setting(View v){
        Intent intent = new Intent(Workspace.this, UserSetting.class);
        startActivity(intent);
    }

    //directory 추가 버튼
    public void dir_add(View v){
        Intent intent = new Intent(Workspace.this, Directory_Add.class);
        startActivity(intent);
    }

    //navigation item
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    //navigation item dynamic
    private void directoryList(String display_name) {
        Call<ArrayList<DirectoryResponse>> dirlist = service.dirList(display_name);
        dirlist.enqueue(new Callback<ArrayList<DirectoryResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<DirectoryResponse>> call, Response<ArrayList<DirectoryResponse>> response) {
                Log.d("통신성공"," "+new Gson().toJson(response.body()));
                DirListAdapter dir_Adapter = new DirListAdapter(response.body());
                mRecyclerView.setAdapter(dir_Adapter);
            }
            @Override
            public void onFailure(Call<ArrayList<DirectoryResponse>> call, Throwable t) {
                Log.d("디렉토리 리스트 통신 실패","");
                t.printStackTrace();
            }
        });
    }
}
