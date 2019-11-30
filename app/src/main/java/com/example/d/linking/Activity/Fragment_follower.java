package com.example.d.linking.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.d.linking.Adapter.FollowerAdapter;
import com.example.d.linking.Data.FollowerResponse;
import com.example.d.linking.R;
import com.example.d.linking.Server.APIClient;
import com.example.d.linking.Server.APIInterface;
import com.google.gson.Gson;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_follower extends Fragment {
    private SharedPreferences preferences;
    String display_name;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private APIInterface service;
    Context mContext;

    public Fragment_follower(){}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //ViewGroup rootView = (ViewGroup) getLayoutInflater().inflate(R.layout.fragment_workspace, container, false);
        View view = inflater.inflate(R.layout.custom_tab, container, false);

        preferences = this.getActivity().getSharedPreferences("user",MODE_PRIVATE);
        display_name = preferences.getString("display_name","");

        //server connection
        service= APIClient.getClient().create(APIInterface.class);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.follow);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        followerList(display_name);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public void followerList(String display_name){
        Call<ArrayList<FollowerResponse>> follower = service.followerlist(display_name);
        follower.enqueue(new Callback<ArrayList<FollowerResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<FollowerResponse>> call, Response<ArrayList<FollowerResponse>> response) {
                Log.d("통신성공111"," "+new Gson().toJson(response.body()));
                Log.d("wwwww","ddd");
                FollowerAdapter adapter = new FollowerAdapter(response.body());
                mRecyclerView.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<ArrayList<FollowerResponse>> call, Throwable t) {
                Log.d("팔로워 리스트 통신 실패","");
                t.printStackTrace();
            }
        });
    }
}
