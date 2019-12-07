package com.example.d.linking.Adapter;

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
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.example.d.linking.Activity.Link_edit_popup;
import com.example.d.linking.Data.LinkListResponse;
import com.example.d.linking.R;
import com.example.d.linking.Server.APIClient;
import com.example.d.linking.Server.APIInterface;
import com.google.gson.Gson;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class LinkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeRefreshLayout.OnRefreshListener{
    private SharedPreferences preferences;
    private Bitmap bitmap;
    private String url;
    private APIInterface service ;
    int[] link_id = new int[1000];
    int[] favorite = new int[1000];
    int[] read = new int[1000];
    int dir_id;
    int editPosition;
    String display_name;
    Context mContext;

    @Override
    public void onRefresh() {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView read_status;
        TextView meta_title, meta_desc, link_url, desc;
        ImageView meta_imgUrl, img_favorite, link_edit, btn_read, btn_favorite, btn_delete;
        LinearLayout link_click;
        SwipeLayout item;
        private RecyclerView mRecyclerView;
        private RecyclerView.LayoutManager mLayoutManager;

        public MyViewHolder(View view){
            super(view);
            mContext = view.getContext();

            preferences = mContext.getSharedPreferences("user", MODE_PRIVATE);
            dir_id = preferences.getInt("dir_id",0);
            display_name = preferences.getString("display_name", "");

            read_status = (CircleImageView) view.findViewById(R.id.read_status);
            meta_title = (TextView) view.findViewById(R.id.meta_title);
            meta_desc = (TextView) view.findViewById(R.id.meta_desc);
            link_url = (TextView) view.findViewById(R.id.link_url);
            meta_imgUrl = (ImageView) view.findViewById(R.id.meta_imgUrl);
            desc = (TextView) view.findViewById(R.id.desc);
            img_favorite = (ImageView) view.findViewById(R.id.img_favorite);
            item = (SwipeLayout) view.findViewById(R.id.swipe_item);

            link_click = (LinearLayout) view.findViewById(R.id.link_click);
            link_edit = (ImageButton) view.findViewById(R.id.link_edit);

            btn_read = (ImageButton) item.findViewById(R.id.btn_read);
            btn_favorite = (ImageButton) item.findViewById(R.id.btn_favorite);
            btn_delete = (ImageButton) item.findViewById(R.id.btn_delete);

            item.setShowMode(SwipeLayout.ShowMode.PullOut);
            item.addDrag(SwipeLayout.DragEdge.Left, item.findViewById(R.id.bottom_wrapper_2));

            mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            mRecyclerView = view.findViewById(R.id.link_tag);
            mRecyclerView.setLayoutManager(mLayoutManager);

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
        favorite[position]= linkList.get(position).getFavorite_status();
        read[position] = linkList.get(position).getRead_status();

        ArrayList<String> tag = new ArrayList<String>(linkList.get(position).getLink_tag());
        TagListAdapter adapter = new TagListAdapter(tag);
        myViewHolder.mRecyclerView.setAdapter(adapter);

        //즐겨찾기 아닐때
        if(favorite[position]== 0){
            myViewHolder.img_favorite.setVisibility(View.INVISIBLE);
            myViewHolder.btn_favorite.setImageResource(R.drawable.favorite2);
        }else{ //즐겨찾기 일때
            myViewHolder.btn_favorite.setImageResource(R.drawable.unfavorite2);
        }

        link_id[position] = linkList.get(position).getLink_id();
        if(read[position] == 1){
            myViewHolder.read_status.setImageResource(R.drawable.read_1);
            myViewHolder.btn_read.setImageResource(R.drawable.unread);
        }else{
            myViewHolder.read_status.setImageResource(R.drawable.read_0);
            myViewHolder.btn_read.setImageResource(R.drawable.read);
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

        //링크 상태 변경
        myViewHolder.btn_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(read[position] == 1){
                    readStatus(link_id[position]);
                    myViewHolder.read_status.setImageResource(R.drawable.read_0);
                    myViewHolder.btn_read.setImageResource(R.drawable.read);
                    read[position] = 0;
                }else{
                    unread(link_id[position]);
                    myViewHolder.read_status.setImageResource(R.drawable.read_1);
                    myViewHolder.btn_read.setImageResource(R.drawable.unread);
                    read[position] = 1;
                }
            }
        });

        //favorite 상태 변경
        myViewHolder.btn_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favorite[position] == 0) {
                    favorite(display_name,link_id[position]);
                    myViewHolder.img_favorite.setVisibility(View.VISIBLE);
                    myViewHolder.btn_favorite.setImageResource(R.drawable.unfavorite2);
                    favorite[position] = 1;
                }else{
                    if(dir_id == 1){
                        linkList.remove(position);
                        notifyItemRemoved(position);
                    }else{
                        favorite(display_name, link_id[position]);
                        myViewHolder.img_favorite.setVisibility(View.INVISIBLE);
                        myViewHolder.btn_favorite.setImageResource(R.drawable.favorite2);
                        favorite[position] = 0;
                    }
                }
            }
        });

        //링크 삭제
        myViewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return linkList.size();
    }

    public void remove(int swipedPosition) {
        service.linkdelete(dir_id,link_id[swipedPosition]).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("링크 삭제 결과",""+new Gson().toJson(response.code()));
                Toast.makeText(mContext, "링크가 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });

        linkList.remove(swipedPosition);
        notifyItemRemoved(swipedPosition);

    }

    public void readStatus(int linkID){
        service.linkstate(linkID).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("링크 상태 변경 결과",""+new Gson().toJson(response.code()));
                Toast.makeText(mContext, "링크 상태가 변경되었습니다.", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    public void unread(int linkID){
        service.linkunread(linkID).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("링크 상태 변경 결과",""+new Gson().toJson(response.code()));
                Toast.makeText(mContext, "링크 상태가 변경되었습니다.", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    public void favorite(String display_name, int linkID){
        service.linkfavoritestatus(display_name, linkID).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("즐겨찾기 상태 변경 결과",""+new Gson().toJson(response.code()));
                Toast.makeText(mContext, "즐겨찾기 상태가 변경되었습니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
