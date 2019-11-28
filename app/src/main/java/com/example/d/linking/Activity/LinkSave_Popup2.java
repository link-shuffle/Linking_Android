package com.example.d.linking.Activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.d.linking.Data.LinkAddData;
import com.example.d.linking.Data.LinkAddResponse;
import com.example.d.linking.R;
import com.example.d.linking.Server.APIClient;
import com.example.d.linking.Server.APIInterface;
import com.google.gson.Gson;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LinkSave_Popup2 extends Activity {
    private SharedPreferences preferences;
    Button btn_discard2, btn_save2;
    EditText tag2, desc2, edit_url2;
    int dir_id;
    private APIInterface service ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀 바 삭제.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_linksave_popup2);

        preferences = getSharedPreferences("user",MODE_PRIVATE);
        dir_id = preferences.getInt("dir_id",200); //devalue 수정해야돼.

        btn_discard2 = (Button) findViewById(R.id.btn_discard2);
        btn_save2 = (Button) findViewById(R.id.btn_save2);
        tag2 = (EditText) findViewById(R.id.edit_tag2);
        desc2 = (EditText) findViewById(R.id.edit_des2);
        edit_url2 = (EditText) findViewById(R.id.edit_url2);

        service= APIClient.getClient().create(APIInterface.class);

        //배경 제거
        WindowManager.LayoutParams  layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags  = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount  = 0.8f;
        getWindow().setAttributes(layoutParams);

        //dialog size 설정
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        getWindow().getAttributes().width = (int) (dm.widthPixels * 0.9);
        getWindow().getAttributes().height = (int) (dm.heightPixels * 0.5);

        btn_discard2 = findViewById(R.id.btn_discard2);
        btn_discard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_save2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_url2.getText().toString().length() == 0){
                    Toast.makeText(LinkSave_Popup2.this, "URL을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    linkAdd(new LinkAddData(edit_url2.getText().toString(), tag2.getText().toString(), desc2.getText().toString()));
                    //Fragment_workspace refresh = new Fragment_workspace();
                    //refresh.onRefresh();
                    finish();
                }
            }
        });
    }

    // linkadd
    private void linkAdd(LinkAddData data) {
        service.linkadd(dir_id,data).enqueue(new Callback<LinkAddResponse>() {
            @Override
            public void onResponse(Call<LinkAddResponse> call, Response<LinkAddResponse> response) {
                    Toast.makeText(LinkSave_Popup2.this, "Save Success", Toast.LENGTH_SHORT).show();
                    Log.d("링크 저장 결과",""+new Gson().toJson(response.code()));
            }
            @Override
            public void onFailure(Call<LinkAddResponse> call, Throwable t) {
            }
        });
    }
}
