package com.example.d.linking.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.d.linking.Data.MailData;
import com.example.d.linking.Data.MailListResponse;
import com.example.d.linking.R;
import com.example.d.linking.Server.APIClient;
import com.example.d.linking.Server.APIInterface;
import com.google.gson.Gson;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class MailListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    private APIInterface service;
    private SharedPreferences preferences;
    String display_name;

    private ArrayList<MailListResponse> mailList;
    public MailListAdapter(ArrayList<MailListResponse> mailList){
        this.mailList = mailList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mail_display, mail_msg;
        Button mail_accept, mail_delete;

        public MyViewHolder(View view){
            super(view);
            mContext = view.getContext();
            service= APIClient.getClient().create(APIInterface.class);

            preferences = mContext.getSharedPreferences("user", MODE_PRIVATE);
            display_name = preferences.getString("display_name","");

            mail_display = (TextView) view.findViewById(R.id.mail_display);
            mail_msg = (TextView) view.findViewById(R.id.mail_msg);
            mail_delete = (Button) view.findViewById(R.id.mail_delete);
            mail_accept = (Button) view.findViewById(R.id.mail_accept);

        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_mail, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder myViewHolder = (MyViewHolder) holder;

        myViewHolder.mail_display.setText(mailList.get(position).getSender());
        myViewHolder.mail_msg.setText(mailList.get(position).getMessage());
        if(mailList.get(position).getStatus() == 0 ){
            myViewHolder.mail_delete.setVisibility(View.GONE);
            myViewHolder.mail_accept.setVisibility(View.GONE);
        }

        myViewHolder.mail_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail(display_name, mailList.get(position).getSender(), 1, new MailData(mailList.get(position).getMail_id(), mailList.get(position).getDir_id()));
                mailList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,mailList.size());
            }
        });

        myViewHolder.mail_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail(display_name, mailList.get(position).getSender(), 2, new MailData(mailList.get(position).getMail_id(),mailList.get(position).getDir_id()));
                mailList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,mailList.size());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mailList.size();
    }

    public void mail(String display_name, String name, int type, MailData data){
        service.mailresponse(display_name, name, type, data).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("공유 거절 결과",""+new Gson().toJson(response.code()));
                if(type == 1){
                    Toast.makeText(mContext, "공유가 거절되었습니다.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(mContext, "공유가 수락되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    public void remove(int swipedPosition) {
        service.maildelete(mailList.get(swipedPosition).getMail_id()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("메시지 삭제 결과",""+new Gson().toJson(response.code()));
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });

        mailList.remove(swipedPosition);
        notifyItemRemoved(swipedPosition);
    }

}
