package com.example.d.linking.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.d.linking.R;

import java.util.ArrayList;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TagListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<String> tag;
    public TagListAdapter(ArrayList<String> tag) {
        this.tag = tag;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button btn_tag;

        public MyViewHolder(View view){
            super(view);

            btn_tag = (Button) view.findViewById(R.id.btn_tag);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_tag, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder myViewHolder = (MyViewHolder) holder;

        myViewHolder.btn_tag.setText(tag.get(position));
    }

    @Override
    public int getItemCount() {
        return tag.size();
    }
}
