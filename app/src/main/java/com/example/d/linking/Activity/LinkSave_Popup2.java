package com.example.d.linking.Activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import com.example.d.linking.Server.APIInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LinkSave_Popup2 extends Activity {
    Button btn_discard2, btn_save2;
    EditText tag2, desc2, edit_url2;
    private APIInterface service ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀 바 삭제.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_linksave_popup2);

        btn_discard2 = (Button) findViewById(R.id.btn_discard);
        btn_save2 = (Button) findViewById(R.id.btn_save2);
        tag2 = (EditText) findViewById(R.id.edit_tag);
        desc2 = (EditText) findViewById(R.id.edit_des);
        edit_url2 = (EditText) findViewById(R.id.edit_url2);

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
                if(edit_url2.getText().toString() != null){
                    linkAdd(new LinkAddData("www.naver.com", tag2.getText().toString(), desc2.getText().toString()));
                }else{
                    Toast.makeText(LinkSave_Popup2.this, "URL을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // linkadd
    private void linkAdd(LinkAddData data) {
        service.linkadd(200,data).enqueue(new Callback<LinkAddResponse>() {
            @Override
            public void onResponse(Call<LinkAddResponse> call, Response<LinkAddResponse> response) {
                    Toast.makeText(LinkSave_Popup2.this, "Save Success", Toast.LENGTH_SHORT).show();
                    finish();
            }
            @Override
            public void onFailure(Call<LinkAddResponse> call, Throwable t) {
            }
        });
    }
}
