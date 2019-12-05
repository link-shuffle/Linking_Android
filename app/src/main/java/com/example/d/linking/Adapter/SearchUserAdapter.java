package com.example.d.linking.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.d.linking.Data.SearchUserResponse;
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
import static com.example.d.linking.R.color.actionbarcolor;

public class SearchUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    private APIInterface service;
    private SharedPreferences preferences;
    String display_name;

    private ArrayList<SearchUserResponse> searchuser;
    public SearchUserAdapter(ArrayList<SearchUserResponse> searchuser){
        this.searchuser =searchuser;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView searchuser_display, searchuser_name;
        Button btn_followuser;
        public MyViewHolder(View view){
            super(view);
            mContext = view.getContext();
            service= APIClient.getClient().create(APIInterface.class);

            preferences = mContext.getSharedPreferences("user", MODE_PRIVATE);
            display_name = preferences.getString("display_name","");

            searchuser_display = (TextView) view.findViewById(R.id.searchuser_display);
            searchuser_name = (TextView) view.findViewById(R.id.searchuser_name);
            btn_followuser = (Button) view.findViewById(R.id.btn_followuser);

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_searchuser, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final SearchUserAdapter.MyViewHolder myViewHolder = (SearchUserAdapter.MyViewHolder) holder;

        myViewHolder.searchuser_display.setText(searchuser.get(position).getDisplay_name());
        myViewHolder.searchuser_name.setText(searchuser.get(position).getName());
        if(searchuser.get(position).getFollowing_status() == 1){
            myViewHolder.btn_followuser.setText("팔로잉");
            myViewHolder.btn_followuser.setBackgroundResource(R.drawable.btn_following);
        }else{
            myViewHolder.btn_followuser.setTextColor(Color.parseColor("#f4f4f4"));
            myViewHolder.btn_followuser.setBackgroundResource(R.drawable.btn_follow);
            myViewHolder.btn_followuser.setText("팔로우");
        }

        myViewHolder.btn_followuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchuser.get(position).getFollowing_status() == 1){
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(mContext);
                    alert_confirm.setMessage("팔로잉을 취소하시겠습니까?").setCancelable(false).setPositiveButton("팔로잉 취소",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //unfollowing
                                    followingDelete(display_name, searchuser.get(position).getDisplay_name());
                                    myViewHolder.btn_followuser.setTextColor(Color.parseColor("#f4f4f4"));
                                    myViewHolder.btn_followuser.setBackgroundResource(R.drawable.btn_follow);
                                    myViewHolder.btn_followuser.setText("팔로우");
                                }
                            }).setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            });
                    AlertDialog alert = alert_confirm.create();
                    alert.show();

                }else{
                    //follow
                    followUser(display_name, searchuser.get(position).getDisplay_name());
                    myViewHolder.btn_followuser.setBackgroundResource(R.drawable.btn_following);
                    myViewHolder.btn_followuser.setText("팔로잉");
                    myViewHolder.btn_followuser.setTextColor(Color.parseColor("#2c3130"));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return searchuser.size();
    }

    public void followUser(String display_name, String followuser){
        service.followingadd(display_name, followuser).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("팔로잉 추가 결과",""+new Gson().toJson(response.code()));
                Toast.makeText(mContext, "팔로잉 되었습니다.", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    public void followingDelete(String display_name, String following_display){
        service.followingdelete(display_name,following_display).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("팔로잉 삭제 결과",""+new Gson().toJson(response.code()));
                Toast.makeText(mContext, "팔로잉 취소되었습니다.", Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }
}
