package com.example.d.linking.Activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.d.linking.Adapter.DirListAdapter;
import com.example.d.linking.Data.DirectoryResponse;
import com.example.d.linking.R;
import com.example.d.linking.Server.APIClient;
import com.example.d.linking.Server.APIInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.Log;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class User_workspace extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,FragmentCallback {
    Toolbar toolbar;
    FloatingActionButton btn_link_add;
    Fragment workspace;
    private APIInterface service ;
    String display_name;
    //클립보드
    ClipboardManager mClipboard;
    String pasteData;
    //디렉토리 list recycler
    RecyclerView mRecyclerView, mRecyclerView2;
    RecyclerView.LayoutManager mLayoutManager, mLayoutManager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_workspace);
        Intent intent = getIntent();
        display_name = intent.getStringExtra("display_name");


        //server connection
        service= APIClient.getClient().create(APIInterface.class);

        //toolbar 설정
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //클립보드 가져오기.
        mClipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        pasteData = "";
        ClipData.Item item = mClipboard.getPrimaryClip().getItemAt(0);
        pasteData = item.getText().toString();
        if(pasteData != null) {
            Intent intent1 = new Intent(User_workspace.this, LinkSave_Popup.class);
            startActivity(intent1);
        }

        //link 추가 팝업 버튼
        btn_link_add = (FloatingActionButton) findViewById(R.id.link_add);
        btn_link_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(User_workspace.this, LinkSave_Popup2.class);
                startActivity(intent);
            }
        });

        //전체화면 설정
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //네비게이션바 설정.
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //fragment 화면 설정.
        workspace = new Fragment_workspace();
        getSupportFragmentManager().beginTransaction().add(R.id.container, workspace).commit();

        //디렉토리 list 호출
        directoryList(display_name);
    }

    //user setting 버튼
    public void user_setting(View v){
        Intent intent = new Intent(User_workspace.this, UserSetting.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_workspace, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //navigation 목록
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.test) {
            onChangedFragment(1, null);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_share) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //fragment 화면 전환 이벤트
    @Override
    public void onChangedFragment(int position, Bundle bundle) {
        Fragment fragment = null;

        switch (position) {
            case 1:
                fragment = workspace;
                break;
            default:
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    //navigation item dynamic
    private void directoryList(String desplay) {
        Call<ArrayList<DirectoryResponse>> dirlist0 = service.dirList0(desplay);
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
    }

    //navigation item dynamic2
    public void directoryList2(String name, int number) {
        if(name != null) {
            //Call<ArrayList<DirectoryResponse>> dirlist;
            try {
                //dirlist = service.dirListSub(name, number);
                service.dirListSub(name,number).enqueue(new Callback<ArrayList<DirectoryResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<DirectoryResponse>> call, Response<ArrayList<DirectoryResponse>> response) {
                        Log.d("통신성공", " " + new Gson().toJson(response.body()));
                        DirListAdapter dir_Adapter = new DirListAdapter(response.body());
                        mRecyclerView2.setAdapter(dir_Adapter);
                    }

                    @Override
                    public void onFailure(Call<ArrayList<DirectoryResponse>> call, Throwable t) {
                        Log.d("디렉토리 리스트 통신 실패", "");
                        t.printStackTrace();
                    }
                });
            }catch (NullPointerException e){
                Log.d("디스플레이네임",""+name);
            }
        }

    }

}
