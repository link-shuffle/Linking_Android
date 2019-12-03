package com.example.d.linking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.d.linking.Data.SearchUserResponse;
import com.example.d.linking.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;

    private ArrayList<SearchUserResponse> searchuser;
    public SearchUserAdapter(ArrayList<SearchUserResponse> searchuser){
        this.searchuser =searchuser;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView searchuser_display, searchuser_name;
        public MyViewHolder(View view){
            super(view);
            mContext = view.getContext();

            searchuser_display = (TextView) view.findViewById(R.id.searchuser_display);
            searchuser_name = (TextView) view.findViewById(R.id.searchuser_name);
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

    }

    @Override
    public int getItemCount() {
        return searchuser.size();
    }
}
