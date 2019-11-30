package com.example.d.linking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

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

    private ArrayList<FollowerResponse> follower;
    public FollowerAdapter(ArrayList<FollowerResponse> follower){
        this.follower = follower;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView follower_display, follower_name;

        public MyViewHolder(View view){
            super(view);
            mContext = view.getContext();

            follower_display = (TextView) view.findViewById(R.id.follower_display);
            follower_name = (TextView) view.findViewById(R.id.follower_name);
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
    }

    @Override
    public int getItemCount() {
        return follower.size();
    }
}
