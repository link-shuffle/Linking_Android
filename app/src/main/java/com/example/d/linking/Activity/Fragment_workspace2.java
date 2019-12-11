package com.example.d.linking.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.d.linking.Adapter.LinkAdapter;
import com.example.d.linking.Adapter.LinkAdapter2;
import com.example.d.linking.Data.LinkListResponse;
import com.example.d.linking.R;
import com.example.d.linking.Server.APIClient;
import com.example.d.linking.Server.APIInterface;
import com.google.gson.Gson;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_workspace2 extends Fragment{
    private SharedPreferences preferences;
    private int dir_id;
    private String dir_name;
    private String display_name;
    private ProgressBar loadingPanel;
    private Context mContext;
    private TextView text_dirID;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private APIInterface service;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //ViewGroup rootView = (ViewGroup) getLayoutInflater().inflate(R.layout.fragment_workspace, container, false);
        View view = inflater.inflate(R.layout.fragment_workspace2, container, false);

        preferences = this.getActivity().getSharedPreferences("user",MODE_PRIVATE);
        dir_id = preferences.getInt("otherdir_id",0);
        dir_name = preferences.getString("otherdir_name","");
        display_name = preferences.getString("other_user","");

        text_dirID = (TextView) view.findViewById(R.id.directory_name);
        text_dirID.setText(dir_name);

        loadingPanel = (ProgressBar) view.findViewById(R.id.loadingPanel);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recylcer_linkcard);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //server connection
        service= APIClient.getClient().create(APIInterface.class);
        LinkList(dir_id);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    //현재 디렉토리 속 링크 불러오기.
    private void LinkList(int dir_id) {
        Call<ArrayList<LinkListResponse>> linkList = service.linklist(dir_id);
        linkList.enqueue(new Callback<ArrayList<LinkListResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<LinkListResponse>> call, Response<ArrayList<LinkListResponse>> response) {
                Log.d("통신성공"," "+new Gson().toJson(response.body()));
                LinkAdapter2 adapter = new LinkAdapter2(response.body());
                mRecyclerView.setAdapter(adapter);
                loadingPanel.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onFailure(Call<ArrayList<LinkListResponse>> call, Throwable t) {
                Log.d("디렉토리 리스트 통신 실패","");
                t.printStackTrace();
            }
        });
    }
}