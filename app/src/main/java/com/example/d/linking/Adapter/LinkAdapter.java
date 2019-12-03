package com.example.d.linking.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.d.linking.Activity.Link_edit_popup;
import com.example.d.linking.Data.LinkListResponse;
import com.example.d.linking.R;
import com.example.d.linking.Server.APIClient;
import com.example.d.linking.Server.APIInterface;
import com.google.gson.Gson;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class LinkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private SharedPreferences preferences;
    private Bitmap bitmap;
    private String url;
    private APIInterface service ;
    int[] link_id = new int[1000];
    int dir_id;
    int editPosition;
    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView read_status;
        TextView meta_title, meta_desc, link_url, link_tag, desc;
        ImageView meta_imgUrl, img_favorite, link_edit;
        LinearLayout link_click;

        public MyViewHolder(View view){
            super(view);
            mContext = view.getContext();

            preferences = mContext.getSharedPreferences("user", MODE_PRIVATE);
            dir_id = preferences.getInt("dir_id",0);

            read_status = (CircleImageView) view.findViewById(R.id.read_status);
            meta_title = (TextView) view.findViewById(R.id.meta_title);
            meta_desc = (TextView) view.findViewById(R.id.meta_desc);
            link_url = (TextView) view.findViewById(R.id.link_url);
            link_tag = (TextView) view.findViewById(R.id.link_tag);
            meta_imgUrl = (ImageView) view.findViewById(R.id.meta_imgUrl);
            desc = (TextView) view.findViewById(R.id.desc);
            img_favorite = (ImageView) view.findViewById(R.id.img_favorite);

            link_click = (LinearLayout) view.findViewById(R.id.link_click);
            link_edit = (ImageButton) view.findViewById(R.id.link_edit);

            service= APIClient.getClient().create(APIInterface.class);
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
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;

        myViewHolder.link_url.setText(linkList.get(position).getLink());
        myViewHolder.meta_desc.setText(linkList.get(position).getMeta_desc());
        myViewHolder.meta_title.setText(linkList.get(position).getMeta_title());
        myViewHolder.desc.setText(linkList.get(position).getDesc());
        myViewHolder.link_tag.setText(linkList.get(position).getLink_tag());
        if(linkList.get(position).getFavorite_status() == 0){
            myViewHolder.img_favorite.setVisibility(View.INVISIBLE);
        }
        link_id[position] = linkList.get(position).getLink_id();
        if(linkList.get(position).getRead_status() == 1){
            myViewHolder.read_status.setImageResource(R.drawable.read_1);
        }else{
            myViewHolder.read_status.setImageResource(R.drawable.read_0);
        }
        url = linkList.get(position).getMeta_imgUrl();

        LoadImage loadImage = new LoadImage(url);
        bitmap = loadImage.getBitmap();
        myViewHolder.meta_imgUrl.setImageBitmap(bitmap);

        //link 수정
        myViewHolder.link_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Link_edit_popup.class);
                intent.putExtra("link_id",link_id[position]);
                intent.putExtra("tag",linkList.get(position).getLink_tag());
                intent.putExtra("desc", linkList.get(position).getDesc());
                mContext.startActivity(intent);
            }
        });

        //link 연결
        myViewHolder.link_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //읽음 처리
                editPosition = position;
                readStatus(link_id[position]);
                myViewHolder.read_status.setImageResource(R.drawable.read_0);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkList.get(position).getLink()));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return linkList.size();
    }

    public void remove(int swipedPosition) {
        linkList.remove(swipedPosition);
        notifyItemRemoved(swipedPosition);

        service.linkdelete(dir_id,link_id[swipedPosition]).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("링크 삭제 결과",""+new Gson().toJson(response.code()));
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });

    }

    public void readStatus(int linkID){
        service.linkstate(linkID).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("링크 상태 변경 결과",""+new Gson().toJson(response.code()));
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    public void favorite(int swipedPosition){

    }
}
