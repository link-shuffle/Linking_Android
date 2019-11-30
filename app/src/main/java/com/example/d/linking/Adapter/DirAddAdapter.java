package com.example.d.linking.Adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.d.linking.Activity.DirSave_Popup;
import com.example.d.linking.Activity.Directory_Add;
import com.example.d.linking.Activity.Directory_Add2;
import com.example.d.linking.Activity.Directory_Rename;
import com.example.d.linking.Activity.MainActivity;
import com.example.d.linking.Activity.Workspace;
import com.example.d.linking.Data.DirectoryResponse;
import com.example.d.linking.R;
import com.example.d.linking.Server.APIClient;
import com.example.d.linking.Server.APIInterface;
import com.google.gson.Gson;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class DirAddAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    int[] array = new int[1000];
    private SharedPreferences preferences;
    private APIInterface service ;
    String display_name;
    Context mContext;

    private ArrayList<DirectoryResponse> dirAddList;
    public DirAddAdapter(ArrayList<DirectoryResponse> dirAddList){
        this.dirAddList = dirAddList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public Button dir_title;

        public MyViewHolder(View view){
            super(view);
            mContext = view.getContext();
            preferences = mContext.getSharedPreferences("user", MODE_PRIVATE);
            display_name = preferences.getString("display_name","");
            service= APIClient.getClient().create(APIInterface.class);
            dir_title = view.findViewById(R.id.dir_title);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_diradd, parent, false);
        return new DirAddAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final DirAddAdapter.MyViewHolder myViewHolder = (DirAddAdapter.MyViewHolder) holder;
        array[position] = dirAddList.get(position).getDir_id();
        myViewHolder.dir_title.setText(dirAddList.get(position).getName());

        myViewHolder.dir_title.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Directory_Add2.class);
                intent.putExtra("dirID",array[position]);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dirAddList.size();
    }
}
