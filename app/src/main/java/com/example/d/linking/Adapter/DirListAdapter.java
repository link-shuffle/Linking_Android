package com.example.d.linking.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.d.linking.Activity.Workspace;
import com.example.d.linking.Data.DirectoryResponse;
import com.example.d.linking.R;
import com.example.d.linking.Server.APIClient;
import com.example.d.linking.Server.APIInterface;
import com.google.gson.Gson;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class DirListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    int[] array = new int[1000];
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    String display_name;
    Context mContext;

    //디렉토리 list recycler
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private APIInterface service;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public Button dir_item;
        public ImageButton arrow_right;

        public MyViewHolder(View view){
            super(view);
            mContext = view.getContext();
            preferences = mContext.getSharedPreferences("user", MODE_PRIVATE);
            display_name = preferences.getString("display_name","");
            dir_item = (Button) view.findViewById(R.id.dir_item);
            arrow_right = (ImageButton) view.findViewById(R.id.arrow_right);

            //디렉토리 list recycler
            mRecyclerView = view.findViewById(R.id.nav_recycleSub1);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(mContext);
            mRecyclerView.setLayoutManager(mLayoutManager);

            //server connection
            service= APIClient.getClient().create(APIInterface.class);
        }
    }

    private ArrayList<DirectoryResponse> dirList;
        public DirListAdapter(ArrayList<DirectoryResponse> dirList){
            this.dirList = dirList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_nav_dir, parent, false);


        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder myViewHolder = (MyViewHolder) holder;

        myViewHolder.dir_item.setText(dirList.get(position).getName());
        array[position] = dirList.get(position).getDir_id(); //directory id

        myViewHolder.dir_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = preferences.edit();
                editor.putInt("dir_id", array[position]);
                editor.putString("dir_name",dirList.get(position).getName());
                editor.commit();
                Intent intent = new Intent(v.getContext(),Workspace.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            }
        });

        myViewHolder.arrow_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewHolder.arrow_right.setImageResource(R.drawable.arrow_below);
                //디렉토리 list 호출
                if(display_name != null){
                    directoryList2(display_name,array[position]);
                }
            }
        });

    }

    //navigation item dynamic2
    public void directoryList2(String name, int dirID) {
        try {
            service.dirListSub(name,dirID).enqueue(new Callback<ArrayList<DirectoryResponse>>() {
                @Override
                public void onResponse(Call<ArrayList<DirectoryResponse>> call, Response<ArrayList<DirectoryResponse>> response) {
                    Log.d("통신성공", " " + new Gson().toJson(response.body()));
                    if(response.body() != null) {
                        DirListAdapter dir_Adapter = new DirListAdapter(response.body());
                        mRecyclerView.setAdapter(dir_Adapter);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<DirectoryResponse>> call, Throwable t) {
                    Log.d("디렉토리 리스트 통신 실패", "");
                    t.printStackTrace();
                }
            });

        }catch (NullPointerException e){
            Log.d("디스플레이네임",""+name);
            return;
        }
    }

    @Override
    public int getItemCount() { return dirList.size(); }
}
