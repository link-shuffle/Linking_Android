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
        public ImageButton dir_title, dir_delete;
        public Button dir_rename;

        public MyViewHolder(View view){
            super(view);
            mContext = view.getContext();
            preferences = mContext.getSharedPreferences("user", MODE_PRIVATE);
            display_name = preferences.getString("display_name","");

            service= APIClient.getClient().create(APIInterface.class);

            dir_title = view.findViewById(R.id.dir_title);
            dir_delete = view.findViewById(R.id.dir_delete);
            dir_rename = view.findViewById(R.id.dir_rename);

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
        myViewHolder.dir_rename.setText(dirAddList.get(position).getName());
        array[position] = dirAddList.get(position).getDir_id();

        myViewHolder.dir_title.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Directory_Add2.class);
                intent.putExtra("dirID",array[position]);
                mContext.startActivity(intent);
            }
        });
        myViewHolder.dir_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(mContext);
                alert_confirm.setMessage("디렉토리를 삭제하시겠습니까?").setCancelable(false).setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Dirdelete(array[position]);
                                Intent intent = new Intent(v.getContext(),Workspace.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                mContext.startActivity(intent);
                            }
                        }).setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();
            }
        });

        myViewHolder.dir_rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Directory_Rename.class);
                intent.putExtra("dirID",array[position]);
                intent.putExtra("dirName","");
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dirAddList.size();
    }

    public void Dirdelete(int dir_id){
        service.dirdelete(display_name,dir_id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("디렉토 삭제 결과",""+new Gson().toJson(response.code()));
                Toast.makeText(mContext, "delete success", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }}
