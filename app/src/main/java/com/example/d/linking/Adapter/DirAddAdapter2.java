package com.example.d.linking.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.d.linking.Activity.DirSave_Popup;
import com.example.d.linking.Activity.LinkSave_Popup;
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

public class DirAddAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    int[] array = new int[1000];
    int[] array_arrow = new int[1000];
    ArrayList<RecyclerView> recycles = new ArrayList<>();
    private SharedPreferences preferences;
    private APIInterface service ;
    String display_name;
    Context mContext;

    private ArrayList<DirectoryResponse> dirAddList;
    public DirAddAdapter2(ArrayList<DirectoryResponse> dirAddList){
        this.dirAddList = dirAddList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public Button dir_title;
        public ImageButton arrow_right2;
        private RecyclerView mRecyclerView;
        private RecyclerView.LayoutManager mLayoutManager;

        public MyViewHolder(View view){
            super(view);
            mContext = view.getContext();
            preferences = mContext.getSharedPreferences("user", MODE_PRIVATE);
            display_name = preferences.getString("display_name","");
            service= APIClient.getClient().create(APIInterface.class);

            dir_title = view.findViewById(R.id.dir_title);
            arrow_right2 = view.findViewById(R.id.arrow_right2);

            //디렉토리 list recycler
            mRecyclerView = view.findViewById(R.id.nav_recycleSub2);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(mContext);
            mRecyclerView.setLayoutManager(mLayoutManager);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_diradd, parent, false);
        return new DirAddAdapter2.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final DirAddAdapter2.MyViewHolder myViewHolder = (DirAddAdapter2.MyViewHolder) holder;
        array_arrow[position]=0;
        array[position] = dirAddList.get(position).getDir_id();
        myViewHolder.dir_title.setText(dirAddList.get(position).getName());
        recycles.add(myViewHolder.mRecyclerView);

        myViewHolder.dir_title.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LinkSave_Popup.class);
                intent.putExtra("diradd_id",array[position]);
                mContext.startActivity(intent);
                ((Activity)mContext).finish();
            }
        });

        myViewHolder.arrow_right2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(array_arrow[position] == 0) {
                    myViewHolder.arrow_right2.setImageResource(R.drawable.arrow_below);
                    //디렉토리 list 호출
                    if (display_name != null) {
                        directoryAdd2(display_name, array[position], position);
                        array_arrow[position] = 1;
                    }
                }
                else{
                    myViewHolder.arrow_right2.setImageResource(R.drawable.arrow_right);
                    toggle(position);
                    array_arrow[position] = 0;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dirAddList.size();
    }

    public void directoryAdd2(String name, int dirID, int position) {
        try {
            service.dirListSub(name,dirID).enqueue(new Callback<ArrayList<DirectoryResponse>>() {
                @Override
                public void onResponse(Call<ArrayList<DirectoryResponse>> call, Response<ArrayList<DirectoryResponse>> response) {
                    Log.d("통신성공", " " + new Gson().toJson(response.body()));
                    if(response.body() != null) {
                        DirAddAdapter2 adapter = new DirAddAdapter2(response.body());
                        recycles.get(position).setAdapter(adapter);
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

    public void toggle(int position) {
        service.toggle().enqueue(new Callback<ArrayList<DirectoryResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<DirectoryResponse>> call, Response<ArrayList<DirectoryResponse>> response) {
                Log.d("빈배열","toggle");
                DirListAdapter dir_Adapter = new DirListAdapter(response.body());
                recycles.get(position).setAdapter(dir_Adapter);
            }
            @Override
            public void onFailure(Call<ArrayList<DirectoryResponse>> call, Throwable t) {

            }
        });
    }
}
