package com.example.d.linking.Adapter;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.d.linking.Data.LinkListResponse;
import com.example.d.linking.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class LinkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Bitmap bitmap;
    private String url;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView read_status;
        TextView meta_title, meta_desc, link_url, link_tag, desc;
        ImageView meta_imgUrl;

        public MyViewHolder(View view){
            super(view);
            read_status = (CircleImageView) view.findViewById(R.id.read_status);
            meta_title = (TextView) view.findViewById(R.id.meta_title);
            meta_desc = (TextView) view.findViewById(R.id.meta_desc);
            link_url = (TextView) view.findViewById(R.id.link_url);
            link_tag = (TextView) view.findViewById(R.id.link_tag);
            meta_imgUrl = (ImageView) view.findViewById(R.id.meta_imgUrl);
            desc = (TextView) view.findViewById(R.id.desc);
        }
    }

    private ArrayList<LinkListResponse> linkList;
    public LinkAdapter(ArrayList<LinkListResponse> linkList){
        this.linkList = linkList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_link, parent, false);
        return new LinkAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;

        myViewHolder.link_url.setText(linkList.get(position).getLink());
        myViewHolder.meta_desc.setText(linkList.get(position).getMeta_desc());
        myViewHolder.meta_title.setText(linkList.get(position).getMeta_title());
        myViewHolder.desc.setText(linkList.get(position).getDesc());
        myViewHolder.link_tag.setText(linkList.get(position).getLink_tag());
        if(linkList.get(position).getRead_status() == 1){
            myViewHolder.read_status.setImageResource(R.drawable.read_1);
        }else{
            myViewHolder.read_status.setImageResource(R.drawable.read_0);
        }
        url = linkList.get(position).getMeta_imgUrl();

        LoadImage loadImage = new LoadImage(url);
        bitmap = loadImage.getBitmap();
        myViewHolder.meta_imgUrl.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return linkList.size();
    }
}
