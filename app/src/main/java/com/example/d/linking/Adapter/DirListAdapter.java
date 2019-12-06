package com.example.d.linking.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.d.linking.Activity.DirSave_Popup;
import com.example.d.linking.Activity.Directory_Rename;
import com.example.d.linking.Activity.Search_user;
import com.example.d.linking.Activity.Workspace;
import com.example.d.linking.Data.DirectoryResponse;
import com.example.d.linking.R;
import com.example.d.linking.Server.APIClient;
import com.example.d.linking.Server.APIInterface;
import com.google.gson.Gson;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class DirListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    int[] array = new int[1000];
    int[] array_auth = new int[1000];
    int[] array_arrow = new int[1000];
    ArrayList<RecyclerView> recycles = new ArrayList<>();
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    String display_name;
    Context mContext;
    private APIInterface service;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public Button dir_item;
        public ImageButton arrow_right, dir_option, dir_share;
        private RecyclerView mRecyclerView;
        private RecyclerView.LayoutManager mLayoutManager;

        public MyViewHolder(View view){
            super(view);
            mContext = view.getContext();
            preferences = mContext.getSharedPreferences("user", MODE_PRIVATE);
            display_name = preferences.getString("display_name","");
            dir_item = (Button) view.findViewById(R.id.dir_item);
            dir_option = (ImageButton) view.findViewById(R.id.dir_option);
            dir_share = (ImageButton) view.findViewById(R.id.dir_share);
            arrow_right = (ImageButton) view.findViewById(R.id.arrow_right);

            //디렉토리 list recycler
            mRecyclerView = view.findViewById(R.id.nav_recycleSub1);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(mContext);
            mRecyclerView.setLayoutManager(mLayoutManager);

            //server connection
            service= APIClient.getClient().create(APIInterface.class);
        }
    }

    private ArrayList<DirectoryResponse> dirList;
        public DirListAdapter(ArrayList<DirectoryResponse> dirList){
            this.dirList = dirList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_nav_dir, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder myViewHolder = (MyViewHolder) holder;

        myViewHolder.dir_item.setText(dirList.get(position).getName());
        array_arrow[position]=0;
        array[position] = dirList.get(position).getDir_id(); //directory id
        array_auth[position] = dirList.get(position).getDir_type(); //dir_type
        recycles.add(myViewHolder.mRecyclerView);

        myViewHolder.dir_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = preferences.edit();
                editor.putInt("dir_id", array[position]);
                editor.putString("dir_name",dirList.get(position).getName());
                editor.putInt("dir_type", array_auth[position]);
                editor.commit();
                Intent intent = new Intent(v.getContext(),Workspace.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            }
        });

        myViewHolder.arrow_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(array_arrow[position] == 0){
                    myViewHolder.arrow_right.setImageResource(R.drawable.arrow_below);
                    //디렉토리 list 호출
                    if(display_name != null){
                        directoryList2(display_name,array[position], position);
                        array_arrow[position] = 1;
                    }
                }else{
                    myViewHolder.arrow_right.setImageResource(R.drawable.arrow_right);
                    toggle(position);
                    array_arrow[position] = 0;

                }
            }
        });

        myViewHolder.dir_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Search_user.class);
                intent.putExtra("dir_id",array[position]);
                mContext.startActivity(intent);
            }
        });

        //하위 디렉토리 불러오기.
        myViewHolder.dir_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.getMenuInflater().inflate(R.menu.dir_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            //directory 삭제
                            case R.id.delete:
                                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(mContext);
                                alert_confirm.setMessage("디렉토리를 삭제하시겠습니까?").setCancelable(false).setPositiveButton("삭제",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Dirdelete(array[position]);
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
                                break;

                                //디렉토리 수정
                            case R.id.edit:
                                Intent intent = new Intent(v.getContext(), Directory_Rename.class);
                                intent.putExtra("dirID",array[position]);
                                intent.putExtra("dirName","");
                                mContext.startActivity(intent);
                                break;

                            case R.id.add:
                                Intent intent2 = new Intent(v.getContext(), DirSave_Popup.class);
                                intent2.putExtra("dirID",array[position]);
                                mContext.startActivity(intent2);
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

    }

    //navigation item dynamic2
    public void directoryList2(String name, int dirID, int position) {
        try {
            service.dirListSub(name,dirID).enqueue(new Callback<ArrayList<DirectoryResponse>>() {
                @Override
                public void onResponse(Call<ArrayList<DirectoryResponse>> call, Response<ArrayList<DirectoryResponse>> response) {
                    Log.d("통신성공", " " + new Gson().toJson(response.body()));
                    if(response.body() != null) {
                        DirListAdapter dir_Adapter = new DirListAdapter(response.body());
                        recycles.get(position).setAdapter(dir_Adapter);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<DirectoryResponse>> call, Throwable t) {
                    Log.d("디렉토리 리스트 통신 실패", "리스트 통신 실");
                    t.printStackTrace();
                }
            });

        }catch (NullPointerException e){
            return;
        }
    }

    public void Dirdelete(int dir_id){
        service.dirdelete(display_name,dir_id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("디렉토 삭제 결과",""+new Gson().toJson(response.code()));
                Toast.makeText(mContext, "디렉토리 삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    @Override
    public int getItemCount() { return dirList.size(); }

    public void toggle(int position) {
            service.toggle().enqueue(new Callback<ArrayList<DirectoryResponse>>() {
                @Override
                public void onResponse(Call<ArrayList<DirectoryResponse>> call, Response<ArrayList<DirectoryResponse>> response) {
                    Log.d("빈배열","toggle");
                    DirListAdapter dir_Adapter = new DirListAdapter(response.body());
                    recycles.get(position).setAdapter(dir_Adapter);
                }
                @Override
                public void onFailure(Call<ArrayList<DirectoryResponse>> call, Throwable t) {

                }
            });
    }
}
