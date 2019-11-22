package com.example.d.linking.Adapter;

import android.provider.ContactsContract;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.d.linking.Activity.User_workspace;
import com.example.d.linking.Data.DirectoryResponse;
import com.example.d.linking.R;
import com.example.d.linking.Server.APIClient;
import com.example.d.linking.Server.APIInterface;
import com.google.gson.Gson;

import java.text.BreakIterator;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DirListAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public Button dir_item;
        public ImageButton arrow_right;

        public MyViewHolder(View view){
            super(view);
            dir_item = (Button) view.findViewById(R.id.dir_item);
            arrow_right = (ImageButton) view.findViewById(R.id.arrow_right);
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.dir_item.setText(dirList.get(position).getName());

        myViewHolder.dir_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        myViewHolder.arrow_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }

    @Override
    public int getItemCount() { return dirList.size(); }
}
