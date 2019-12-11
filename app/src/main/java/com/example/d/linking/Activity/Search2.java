package com.example.d.linking.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.d.linking.Adapter.LinkAdapter;
import com.example.d.linking.Adapter.SearchUserAdapter;
import com.example.d.linking.Data.LinkListResponse;
import com.example.d.linking.Data.SearchUserResponse;
import com.example.d.linking.R;
import com.example.d.linking.Server.APIClient;
import com.example.d.linking.Server.APIInterface;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Search2 extends AppCompatActivity {
    private SharedPreferences preferences;
    private ImageButton btn_back;
    private TabLayout mTabLayout;
    private Context mContext;
    private String display_name, word="";
    private EditText bar_search;
    private ProgressBar loadingPanel;
    private RecyclerView mRecyclerView;
    private TextView search_result;
    private RecyclerView.LayoutManager mLayoutManager;
    private APIInterface service;
    private int position, currentPosition;
    private String tag;

    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serach);
        mContext = getApplicationContext();
        preferences = getSharedPreferences("user", MODE_PRIVATE);
        display_name = preferences.getString("display_name","");

        Intent intent = getIntent();
        tag = intent.getStringExtra("tag");

        btn_back = (ImageButton)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTabLayout = (TabLayout) findViewById(R.id.layout_tab2);
        mTabLayout.addTab(mTabLayout.newTab().setText("링크"));
        mTabLayout.addTab(mTabLayout.newTab().setText("사용자"));
        mTabLayout.addTab(mTabLayout.newTab().setText("태그"));
        mTabLayout.getTabAt(2).select();
        currentPosition = 0;

        search_result = (TextView) findViewById(R.id.search_result);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                position = tab.getPosition();
                if(position != currentPosition){
                    if(word.length() != 0) {
                        if (position == 0) {
                            searchAll(display_name, word);
                            currentPosition = position;
                            loadingPanel.setVisibility(View.GONE);
                        } else if (position == 1) {
                            searchUser(display_name, word);
                            currentPosition = position;
                            loadingPanel.setVisibility(View.GONE);
                        } else {
                            searchTag(display_name, word);
                            currentPosition = position;
                            loadingPanel.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float density = displayMetrics.density;
        Drawable ic_id = getResources().getDrawable(R.drawable.search);
        int ic_width = Math.round(14 * density);
        int ic_height = Math.round(14 * density);
        ic_id.setBounds(0, 0, ic_width, ic_height);

        service= APIClient.getClient().create(APIInterface.class);

        loadingPanel = (ProgressBar)findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.GONE);

        mRecyclerView = (RecyclerView) findViewById(R.id.recylcer_search);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        bar_search = (EditText) findViewById(R.id.bar_search);
        bar_search.setText(tag);
        searchTag(display_name, tag);

        ((EditText) findViewById(R.id.bar_search)).setCompoundDrawables(ic_id, null, null, null);
        bar_search.addTextChangedListener(new TextWatcher() {
            int initial;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
                initial = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(bar_search.isFocusable()){
                    try {
                        byte[] bytetext = bar_search.getText().toString().getBytes("KSC5601");
                        word = bar_search.getText().toString();
                        if(initial != bytetext.length){
                            if(s.length() != 0) {
                                if(position == 0){
                                    searchAll(display_name, word);
                                }else if(position == 1){
                                    searchUser(display_name, word);
                                }else{
                                    searchTag(display_name, word);
                                }
                            }else{
                                toggle();
                            }
                            initial = bytetext.length;
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    public void searchUser(String display_name, String keyword){
        search_result.setText("");
        loadingPanel.setVisibility(View.VISIBLE);
        Call<ArrayList<SearchUserResponse>> user = service.searchuser(display_name,keyword);

        user.enqueue(new Callback<ArrayList<SearchUserResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<SearchUserResponse>> call, Response<ArrayList<SearchUserResponse>> response) {
                Log.d("통신성공"," "+new Gson().toJson(response.body()));
                SearchUserAdapter adapter = new SearchUserAdapter(response.body());
                mRecyclerView.invalidate();
                mRecyclerView.setAdapter(adapter);
                loadingPanel.setVisibility(View.GONE);
                search_result.setText("총 "+response.body().size()+"명의 사용자가 검색되었습니다.");
            }
            @Override
            public void onFailure(Call<ArrayList<SearchUserResponse>> call, Throwable t) {
                Log.d(" 통신 실패","검색결과");
                t.printStackTrace();
            }
        });
    }

    public void searchAll(String display_name, String keyword){
        search_result.setText("");
        loadingPanel.setVisibility(View.VISIBLE);
        Call<ArrayList<LinkListResponse>> all = service.searchall(display_name,keyword);

        all.enqueue(new Callback<ArrayList<LinkListResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<LinkListResponse>> call, Response<ArrayList<LinkListResponse>> response) {
                Log.d("통신성공"," "+new Gson().toJson(response.body()));
                LinkAdapter adapter = new LinkAdapter(response.body());
                mRecyclerView.invalidate();
                mRecyclerView.setAdapter(adapter);
                loadingPanel.setVisibility(View.GONE);
                search_result.setText("총 "+response.body().size()+"개의 링크가 검색되었습니다.");
            }
            @Override
            public void onFailure(Call<ArrayList<LinkListResponse>> call, Throwable t) {
                Log.d(" 통신 실패","검색결과");
                t.printStackTrace();
            }
        });
    }

    public void searchTag(String display_name, String keyword){
        search_result.setText("");
        loadingPanel.setVisibility(View.VISIBLE);
        Call<ArrayList<LinkListResponse>> tag = service.searchtag(display_name,keyword);

        tag.enqueue(new Callback<ArrayList<LinkListResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<LinkListResponse>> call, Response<ArrayList<LinkListResponse>> response) {
                Log.d("통신성공"," "+new Gson().toJson(response.body()));
                LinkAdapter adapter = new LinkAdapter(response.body());
                mRecyclerView.invalidate();
                mRecyclerView.setAdapter(adapter);
                loadingPanel.setVisibility(View.GONE);
                search_result.setText("총 "+response.body().size()+"개의 링크가 검색되었습니다.");
            }
            @Override
            public void onFailure(Call<ArrayList<LinkListResponse>> call, Throwable t) {
                Log.d(" 통신 실패","검색결과");
                t.printStackTrace();
            }
        });
    }
    public void toggle(){
        search_result.setText("");
        service.toggleuser().enqueue(new Callback<ArrayList<SearchUserResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<SearchUserResponse>> call, Response<ArrayList<SearchUserResponse>> response) {
                Log.d("빈배열","toggle");
                SearchUserAdapter adapter = new SearchUserAdapter(response.body());
                mRecyclerView.setAdapter(adapter);
                loadingPanel.setVisibility(View.GONE);
                search_result.setText("총 0개가 검색되었습니다.");
            }
            @Override
            public void onFailure(Call<ArrayList<SearchUserResponse>> call, Throwable t) {

            }
        });
    }
}
