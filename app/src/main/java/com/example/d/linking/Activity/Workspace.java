package com.example.d.linking.Activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.d.linking.Adapter.DirListAdapter;
import com.example.d.linking.Adapter.SharedListAdapter;
import com.example.d.linking.Data.BadgeResponse;
import com.example.d.linking.Data.DirectoryResponse;
import com.example.d.linking.R;
import com.example.d.linking.Server.APIClient;
import com.example.d.linking.Server.APIInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Workspace extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ClipboardManager mClipboard;
    Fragment workspace;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    FloatingActionButton btn_link_add;
    Toolbar toolbar;
    String display_name;
    Button user_id;
    TextView badge;
    //디렉토리 list recycler
    RecyclerView mRecyclerView, mRecyclerView2, mRecyclerView3;
    RecyclerView.LayoutManager mLayoutManager, mLayoutManager2, mLayoutManager3;
    private APIInterface service;
    String badge_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace);
        //display_name 가져오기.
        preferences = getSharedPreferences("user", MODE_PRIVATE);
        display_name = preferences.getString("display_name","");

        //fragment 화면 설정.
        workspace = new Fragment_workspace();
        getSupportFragmentManager().beginTransaction().add(R.id.container, workspace).commit();

        //server connection
        service = APIClient.getClient().create(APIInterface.class);
        mClipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        initLayout();

        directoryList(display_name);

        try{
            ClipData.Item item = mClipboard.getPrimaryClip().getItemAt(0);
            String clipdata = item.getText().toString();
            //클립보드
            if(!clipdata.equals(preferences.getString("URL", "")) && URLUtil.isValidUrl(clipdata)){
                Intent intent = new Intent(Workspace.this, LinkSave_DirPopup.class);
                intent.putExtra("URL",clipdata);
                editor = preferences.edit();
                editor.putString("URL",clipdata);
                editor.commit();
                startActivity(intent);
            }
        }catch (NullPointerException e){

        }
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

        return super.onOptionsItemSelected(item);
    }

    //onCreate 초기 설정.
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
        badge = (TextView) header.findViewById(R.id.text_badge);
        service.mailnumber(display_name).enqueue(new Callback<BadgeResponse>() {
            @Override
            public void onResponse(Call<BadgeResponse> call, Response<BadgeResponse> response) {
                Log.d("메일 갯수",""+new Gson().toJson(response.code()));
                try{
                    badge_num = response.body().getMailnumber();
                    badge.setText(badge_num);
                }catch (NullPointerException e){

                }
            }
            @Override
            public void onFailure(Call<BadgeResponse> call, Throwable t) {
            }
        });


        //link 추가 팝업 버튼
        btn_link_add = (FloatingActionButton) findViewById(R.id.link_add);
        btn_link_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Workspace.this, LinkSave_Popup2.class);
                startActivity(intent);
            }
        });

        //디렉토리 list recycler
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

    //search 버튼
    public void search(View v){
        Intent intent = new Intent(Workspace.this, Search.class);
        startActivity(intent);
    }

    //follow 버튼
    public void follow(View v){
        Intent intent = new Intent(Workspace.this, Follow.class);
        startActivity(intent);
    }

    //최상위 디렉토리 추가 버튼
    public void btn_dirplus(View v){
        Intent intent = new Intent(Workspace.this, DirSave_Popup.class);
        intent.putExtra("dirID",0);
        startActivity(intent);
    }

    //favorite 버튼
    public void btn_favorite(View v){
        editor = preferences.edit();
        editor.putInt("dir_id", 1);
        editor.putString("dir_name", "즐겨찾기");
        editor.putInt("dir_type", 2);
        editor.commit();
        Intent intent = new Intent(Workspace.this,Workspace.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //message 버튼
    public void btn_message(View v){
        Intent intent = new Intent(Workspace.this, MailBox.class);
        startActivity(intent);
    }

    //navigation item
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    //navigation item dynamic
    private void directoryList(String display_name) {
        Call<ArrayList<DirectoryResponse>> dirlist0 = service.dirList0(display_name);
        Call<ArrayList<DirectoryResponse>> dirlist1 = service.dirList1(display_name);
        Call<ArrayList<DirectoryResponse>> dirlist2 = service.dirList2(display_name);

        dirlist0.enqueue(new Callback<ArrayList<DirectoryResponse>>() {
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

        dirlist1.enqueue(new Callback<ArrayList<DirectoryResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<DirectoryResponse>> call, Response<ArrayList<DirectoryResponse>> response) {
                Log.d("통신성공"," "+new Gson().toJson(response.body()));
                DirListAdapter dir_Adapter = new DirListAdapter(response.body());
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
                SharedListAdapter dir_Adapter = new SharedListAdapter(response.body());
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
