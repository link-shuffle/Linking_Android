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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.d.linking.Activity.Follow;
import com.example.d.linking.Activity.Fragment_following;
import com.example.d.linking.Activity.Other_Workspace;
import com.example.d.linking.Activity.Workspace;
import com.example.d.linking.Data.FollowerResponse;
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

public class FollowingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private APIInterface service;
    String display_name;
    Context mContext;

    private ArrayList<FollowerResponse> following;
    public FollowingAdapter(ArrayList<FollowerResponse> following){
        this.following = following;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView following_display, following_name;
        Button following_delete;
        LinearLayout other_user;

        public MyViewHolder(View view){
            super(view);
            mContext = view.getContext();

            preferences = mContext.getSharedPreferences("user", MODE_PRIVATE);
            display_name = preferences.getString("display_name","");

            following_display = (TextView) view.findViewById(R.id.following_display);
            following_name = (TextView) view.findViewById(R.id.following_name);
            following_delete = (Button) view.findViewById(R.id.following_delete);
            other_user = (LinearLayout) view.findViewById(R.id.other_user);

            service= APIClient.getClient().create(APIInterface.class);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_following, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final FollowingAdapter.MyViewHolder myViewHolder = (FollowingAdapter.MyViewHolder) holder;

        myViewHolder.following_display.setText(following.get(position).getDisplay_name());
        myViewHolder.following_name.setText(following.get(position).getName());

        myViewHolder.following_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(mContext);
                alert_confirm.setMessage("팔로잉을 취소하시겠습니까?").setCancelable(false).setPositiveButton("팔로잉 취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                followingDelete(display_name, following.get(position).getDisplay_name());
                                following.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position,following.size());
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
            }
        });

        myViewHolder.other_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),Other_Workspace.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                editor = preferences.edit();
                editor.putString("other_user",following.get(position).getDisplay_name());
                editor.putInt("otherdir_id",0);
                editor.putString("otherdir_name","");
                editor.commit();
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return following.size();
    }

    public void followingDelete(String display_name, String following_display){
        service.followingdelete(display_name,following_display).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("팔로잉 삭제 결과",""+new Gson().toJson(response.code()));
                Toast.makeText(mContext, "팔로잉 삭제되었습니다.", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }
}
