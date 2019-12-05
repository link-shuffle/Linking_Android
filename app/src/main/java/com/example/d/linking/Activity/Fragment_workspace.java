package com.example.d.linking.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.d.linking.Adapter.LinkAdapter;
import com.example.d.linking.Data.LinkListResponse;
import com.example.d.linking.R;
import com.example.d.linking.Server.APIClient;
import com.example.d.linking.Server.APIInterface;
import com.google.gson.Gson;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_workspace extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private SharedPreferences preferences;
    int dir_id;
    String dir_name;
    String display_name;
    private Context mContext;
    TextView text_dirID;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private APIInterface service;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private Paint mClearPaint;
    private ColorDrawable mBackground;
    private int backgroundColor;
    private Drawable deleteDrawable;
    private int intrinsicWidth;
    private int intrinsicHeight;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //ViewGroup rootView = (ViewGroup) getLayoutInflater().inflate(R.layout.fragment_workspace, container, false);
        View view = inflater.inflate(R.layout.fragment_workspace, container, false);

        preferences = this.getActivity().getSharedPreferences("user",MODE_PRIVATE);
        dir_id = preferences.getInt("dir_id",0);
        dir_name = preferences.getString("dir_name","");
        display_name = preferences.getString("display_name","");

        text_dirID = (TextView) view.findViewById(R.id.directory_name);
        text_dirID.setText(dir_name);

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
        mBackground = new ColorDrawable();
        backgroundColor = Color.parseColor("#d74444");
        mClearPaint = new Paint();
        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        deleteDrawable = ContextCompat.getDrawable(mContext, R.drawable.delete);
        intrinsicWidth = deleteDrawable.getIntrinsicWidth();
        intrinsicHeight = deleteDrawable.getIntrinsicHeight();
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
                //if (direction == ItemTouchHelper.RIGHT) {

                //}else{

                //}
            }
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX /2, dY, actionState, isCurrentlyActive);


                View itemView = viewHolder.itemView;
                int itemHeight = itemView.getHeight();

                boolean isCancelled = dX == 0 && !isCurrentlyActive;

                if (isCancelled) {
                    clearCanvas(c, itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    return;
                }

                mBackground.setColor(backgroundColor);
                mBackground.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                mBackground.draw(c);

                int deleteIconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                int deleteIconMargin = (itemHeight - intrinsicHeight) / 2;
                int deleteIconLeft = itemView.getRight() - deleteIconMargin - intrinsicWidth;
                int deleteIconRight = itemView.getRight() - deleteIconMargin;
                int deleteIconBottom = deleteIconTop + intrinsicHeight;

                deleteDrawable.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
                deleteDrawable.draw(c);


            }
        };

        //swipe delete
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
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
    private void favorite(String display_name){
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

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //해당 어댑터를 서버와 통신한 값이 나오면 됨
                if(dir_id != 1){
                    LinkList(dir_id);
                }else{
                    favorite(display_name);
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        },3000);
    }

    private void clearCanvas(Canvas c, Float left, Float top, Float right, Float bottom) {
        c.drawRect(left, top, right, bottom, mClearPaint);

    }
}