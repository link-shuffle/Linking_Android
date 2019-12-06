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
import android.widget.Toast;

import com.example.d.linking.Activity.Other_Workspace;
import com.example.d.linking.Data.DirectoryResponse;
import com.example.d.linking.R;
import com.example.d.linking.Server.APIClient;
import com.example.d.linking.Server.APIInterface;
import com.google.gson.Gson;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class DirListAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    int[] array = new int[1000];
    int[] array_arrow = new int[1000];
    ArrayList<RecyclerView> recycles = new ArrayList<>();
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    String display_name;
    Context mContext;
    private APIInterface service;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public Button dir_item;
        public ImageButton arrow_right, dir_option, dir_share;
        private RecyclerView mRecyclerView;
        private RecyclerView.LayoutManager mLayoutManager;

        public MyViewHolder(View view){
            super(view);
            mContext = view.getContext();
            preferences = mContext.getSharedPreferences("user", MODE_PRIVATE);
            display_name = preferences.getString("other_user","");
            dir_item = (Button) view.findViewById(R.id.dir_item);
            dir_option = (ImageButton) view.findViewById(R.id.dir_option);
            dir_share = (ImageButton) view.findViewById(R.id.dir_share);
            arrow_right = (ImageButton) view.findViewById(R.id.arrow_right);

            dir_option.setVisibility(View.GONE);
            dir_share.setVisibility(View.GONE);

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
        public DirListAdapter2(ArrayList<DirectoryResponse> dirList){
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
        array_arrow[position]=0;
        array[position] = dirList.get(position).getDir_id(); //directory id
        recycles.add(myViewHolder.mRecyclerView);

        myViewHolder.dir_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = preferences.edit();
                editor.putInt("otherdir_id", array[position]);
                editor.putString("otherdir_name",dirList.get(position).getName());
                editor.commit();
                Intent intent = new Intent(v.getContext(),Other_Workspace.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            }
        });

        myViewHolder.arrow_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(array_arrow[position] == 0){
                    myViewHolder.arrow_right.setImageResource(R.drawable.arrow_below);
                    //디렉토리 list 호출
                    if(display_name != null){
                        directoryList2(display_name,array[position], position);
                        array_arrow[position] = 1;
                    }
                }else{
                    myViewHolder.arrow_right.setImageResource(R.drawable.arrow_right);
                    toggle(position);
                    array_arrow[position] = 0;

                }
            }
        });
    }

    //navigation item dynamic2
    public void directoryList2(String name, int dirID, int position) {
        try {
            service.dirListSub(name,dirID).enqueue(new Callback<ArrayList<DirectoryResponse>>() {
                @Override
                public void onResponse(Call<ArrayList<DirectoryResponse>> call, Response<ArrayList<DirectoryResponse>> response) {
                    Log.d("통신성공", " " + new Gson().toJson(response.body()));
                    if(response.body() != null) {
                        DirListAdapter2 dir_Adapter = new DirListAdapter2(response.body());
                        recycles.get(position).setAdapter(dir_Adapter);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<DirectoryResponse>> call, Throwable t) {
                    Log.d("디렉토리 리스트 통신 실패", "리스트 통신 실");
                    t.printStackTrace();
                }
            });

        }catch (NullPointerException e){
            return;
        }
    }

    @Override
    public int getItemCount() { return dirList.size(); }

    public void toggle(int position) {
            service.toggle().enqueue(new Callback<ArrayList<DirectoryResponse>>() {
                @Override
                public void onResponse(Call<ArrayList<DirectoryResponse>> call, Response<ArrayList<DirectoryResponse>> response) {
                    Log.d("빈배열","toggle");
                    DirListAdapter2 dir_Adapter = new DirListAdapter2(response.body());
                    recycles.get(position).setAdapter(dir_Adapter);
                }
                @Override
                public void onFailure(Call<ArrayList<DirectoryResponse>> call, Throwable t) {

                }
            });
    }
}
