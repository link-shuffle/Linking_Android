package com.example.d.linking.Activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//extend Activity -> to fullscreen
public class LinkSave_Popup extends Activity{
    Button btn_discard, btn_save;
    TextView text_url;
    EditText tag, desc;
    ClipboardManager mClipboard;
    String pasteData;
    private APIInterface service ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀 바 삭제.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_linksave_popup);

        btn_discard = (Button) findViewById(R.id.btn_discard);
        btn_save = (Button) findViewById(R.id.btn_save);
        text_url = (TextView) findViewById(R.id.text_url);
        mClipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        pasteData = "";

        //배경 제거
        WindowManager.LayoutParams  layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags  = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount  = 0.8f;
        getWindow().setAttributes(layoutParams);

        //dialog size 설정
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        getWindow().getAttributes().width = (int) (dm.widthPixels * 0.9);
        getWindow().getAttributes().height = (int) (dm.heightPixels * 0.5);

        btn_discard = findViewById(R.id.btn_discard);
        btn_discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tag = (EditText) findViewById(R.id.edit_tag);
        desc = (EditText) findViewById(R.id.edit_des);

        service= APIClient.getClient().create(APIInterface.class);

        //클립보드 가져오기.
        try{
            ClipData.Item item = mClipboard.getPrimaryClip().getItemAt(0);
            pasteData = item.getText().toString();
            text_url.setText("URL : "+pasteData);
            btn_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //linkAdd(new LinkAddData(pasteData, tag.getText().toString(), desc.getText().toString()));
                }
            });
        }catch (NullPointerException e){

        }
    }

    // linkadd
    private void linkAdd(LinkAddData data) {
        service.linkadd(200,"",data).enqueue(new Callback<LinkAddResponse>() {
            @Override
            public void onResponse(Call<LinkAddResponse> call, Response<LinkAddResponse> response) {
                //User_workspace result = new User_workspace();
                Toast.makeText(LinkSave_Popup.this, "Save Success", Toast.LENGTH_SHORT).show();
                finish();
            }
            @Override
            public void onFailure(Call<LinkAddResponse> call, Throwable t) {
            }
        });
    }
}