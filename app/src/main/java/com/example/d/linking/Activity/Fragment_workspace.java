package com.example.d.linking.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.d.linking.Adapter.LinkAdapter;
import com.example.d.linking.Data.LinkListResponse;
import com.example.d.linking.R;
import com.example.d.linking.Server.APIClient;
import com.example.d.linking.Server.APIInterface;
import com.google.gson.Gson;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_workspace extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private SharedPreferences preferences;
    private int dir_id, dir_type;
    private ImageButton btn_auth;
    private String dir_name;
    private String display_name;
    private Context mContext;
    private TextView text_dirID;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private APIInterface service;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private Paint mClearPaint;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //ViewGroup rootView = (ViewGroup) getLayoutInflater().inflate(R.layout.fragment_workspace, container, false);
        View view = inflater.inflate(R.layout.fragment_workspace, container, false);

        preferences = this.getActivity().getSharedPreferences("user",MODE_PRIVATE);
        dir_id = preferences.getInt("dir_id",0);
        dir_name = preferences.getString("dir_name","");
        display_name = preferences.getString("display_name","");
        dir_type = preferences.getInt("dir_type",0);

        text_dirID = (TextView) view.findViewById(R.id.directory_name);
        text_dirID.setText(dir_name);

        btn_auth = (ImageButton) view.findViewById(R.id.btn_auth);
        if(dir_type == 0){ //비공개
            btn_auth.setImageResource(R.drawable.btn_private);
        }else if(dir_type == 1){ //공개
            btn_auth.setImageResource(R.drawable.btn_public);
        }else{ //즐겨찾기
            btn_auth.setVisibility(View.GONE);
        }

        btn_auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(mContext);
                if(dir_type == 0) {
                    alert_confirm.setMessage("디렉토리를 공개하시겠습니까?").setCancelable(false).setPositiveButton("공개",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dirAuth(dir_id, dir_name);
                                    Intent intent = new Intent(v.getContext(),Workspace.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    mContext.startActivity(intent);
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
                    alert_confirm.setMessage("디렉토리를 비공개하시겠습니까?").setCancelable(false).setPositiveButton("비공개",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dirAuth(dir_id, dir_name);
                                    Intent intent = new Intent(v.getContext(),Workspace.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    mContext.startActivity(intent);
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
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recylcer_linkcard);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //server connection
        service= APIClient.getClient().create(APIInterface.class);

        //refresh
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.yellow, R.color.red, R.color.black, R.color.blue);

        //link list 불러오기.
        if(dir_id != 1){
            LinkList(dir_id);
        }else{
            favorite(display_name);
        }
        //swipe background custom
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int swipedPosition = viewHolder.getAdapterPosition();
                LinkAdapter adapter = (LinkAdapter) mRecyclerView.getAdapter();
                adapter.remove(swipedPosition);
            }
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX /2, dY, actionState, isCurrentlyActive);

            }
        };
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    //현재 디렉토리 속 링크 불러오기.
    private void LinkList(int dir_id) {
        Call<ArrayList<LinkListResponse>> linkList = service.linklist(dir_id);
        linkList.enqueue(new Callback<ArrayList<LinkListResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<LinkListResponse>> call, Response<ArrayList<LinkListResponse>> response) {
                Log.d("통신성공"," "+new Gson().toJson(response.body()));
                LinkAdapter dir_Adapter = new LinkAdapter(response.body());
                mRecyclerView.setAdapter(dir_Adapter);
            }
            @Override
            public void onFailure(Call<ArrayList<LinkListResponse>> call, Throwable t) {
                Log.d("디렉토리 리스트 통신 실패","");
                t.printStackTrace();
            }
        });
    }

    //favorite link list 불러오기.
    public void favorite(String display_name){
        Call<ArrayList<LinkListResponse>> linkfavorite = service.linkfavorite(display_name);
        linkfavorite.enqueue(new Callback<ArrayList<LinkListResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<LinkListResponse>> call, Response<ArrayList<LinkListResponse>> response) {
                Log.d("통신성공"," "+new Gson().toJson(response.body()));
                LinkAdapter dir_Adapter = new LinkAdapter(response.body());
                mRecyclerView.setAdapter(dir_Adapter);
            }
            @Override
            public void onFailure(Call<ArrayList<LinkListResponse>> call, Throwable t) {
                Log.d("디렉토리 리스트 통신 실패","");
                t.printStackTrace();
            }
        });
    }

    //권한변경
    public void dirAuth(int dir_id, String dir_name){
        service.dirauth(dir_id, dir_name).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("권한 변경 결과",""+new Gson().toJson(response.code()));
                Toast.makeText(mContext, "권한 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext,Workspace.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });

    }
    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //해당 어댑터를 서버와 통신한 값이 나오면 됨
                if (dir_id != 1) {
                    LinkList(dir_id);
                } else {
                    favorite(display_name);
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);
    }
}