package com.example.d.linking.Activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.d.linking.Adapter.DirListAdapter;
import com.example.d.linking.Adapter.SearchUserAdapter;
import com.example.d.linking.Data.DirectoryResponse;
import com.example.d.linking.Data.SearchUserResponse;
import com.example.d.linking.R;
import com.example.d.linking.Server.APIClient;
import com.example.d.linking.Server.APIInterface;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Search_user extends Activity {
    private SharedPreferences preferences;
    private APIInterface service;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private EditText bar_searchuser;
    private String display_name;
    private ImageButton btn_searchuserback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serachuser);
        preferences = getSharedPreferences("user", MODE_PRIVATE);
        display_name = preferences.getString("display_name","");

        //server connection
        service= APIClient.getClient().create(APIInterface.class);

        mRecyclerView = (RecyclerView) findViewById(R.id.recylcer_searchuser);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float density = displayMetrics.density;

        Drawable ic_id = getResources().getDrawable(R.drawable.search);
        int ic_width = Math.round(14 * density);
        int ic_height = Math.round(14 * density);
        ic_id.setBounds(0, 0, ic_width, ic_height);

        btn_searchuserback = (ImageButton) findViewById(R.id.btn_searchuserback);
        btn_searchuserback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bar_searchuser = (EditText) findViewById(R.id.bar_searchuser);
        ((EditText) findViewById(R.id.bar_searchuser)).setCompoundDrawables(ic_id, null, null, null);
        bar_searchuser.addTextChangedListener(new TextWatcher() {
            int initial;
            String word;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
                initial = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(bar_searchuser.isFocusable()){
                    try {
                        byte[] bytetext = bar_searchuser.getText().toString().getBytes("KSC5601");
                        word = bar_searchuser.getText().toString();
                        if(initial != bytetext.length){
                            if(s.length() != 0) {
                                searchUser(display_name, word);
                            }else{
                                mRecyclerView.invalidate();
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
        Call<ArrayList<SearchUserResponse>> user = service.searchuser(display_name,keyword);

        user.enqueue(new Callback<ArrayList<SearchUserResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<SearchUserResponse>> call, Response<ArrayList<SearchUserResponse>> response) {
                Log.d("통신성공"," "+new Gson().toJson(response.body()));
                SearchUserAdapter adapter = new SearchUserAdapter(response.body());
                mRecyclerView.invalidate();
                mRecyclerView.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<ArrayList<SearchUserResponse>> call, Throwable t) {
                Log.d(" 통신 실패","검색결과");
                t.printStackTrace();
            }
        });
    }
}
