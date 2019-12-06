package com.example.d.linking.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.d.linking.Activity.Other_Workspace;
import com.example.d.linking.Data.DirectoryResponse;
import com.example.d.linking.Data.FollowerResponse;
import com.example.d.linking.R;
import com.example.d.linking.Server.APIClient;
import com.example.d.linking.Server.APIInterface;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;

public class FollowerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private ArrayList<FollowerResponse> follower;
    public FollowerAdapter(ArrayList<FollowerResponse> follower){
        this.follower = follower;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView follower_display, follower_name;
        LinearLayout other_user2;

        public MyViewHolder(View view){
            super(view);
            mContext = view.getContext();
            preferences = mContext.getSharedPreferences("user", MODE_PRIVATE);

            follower_display = (TextView) view.findViewById(R.id.follower_display);
            follower_name = (TextView) view.findViewById(R.id.follower_name);
            other_user2 = (LinearLayout) view.findViewById(R.id.other_user2);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_follower, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final FollowerAdapter.MyViewHolder myViewHolder = (FollowerAdapter.MyViewHolder) holder;

        myViewHolder.follower_display.setText(follower.get(position).getDisplay_name());
        myViewHolder.follower_name.setText(follower.get(position).getName());

        myViewHolder.other_user2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),Other_Workspace.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                editor = preferences.edit();
                editor.putString("other_user",follower.get(position).getDisplay_name());
                editor.putInt("otherdir_id",0);
                editor.putString("otherdir_name","");
                editor.commit();
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return follower.size();
    }
}
