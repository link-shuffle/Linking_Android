package com.example.d.linking;

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
import android.widget.TextView;

//extend Activity -> to fullscreen
public class LinkSave_Popup extends Activity{
    Button btn_discard;
    TextView text_url;
    ClipboardManager mClipboard;
    String pasteData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀 바 삭제.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_linksave_popup);

        btn_discard = (Button) findViewById(R.id.btn_discard);
        text_url = (TextView) findViewById(R.id.text_url);
        mClipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        pasteData = "";

        //배경 제거
        WindowManager.LayoutParams  layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags  = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount  = 0.7f;
        getWindow().setAttributes(layoutParams);

        //dialog size 설정
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        getWindow().getAttributes().width = (int) (dm.widthPixels * 0.9);
        getWindow().getAttributes().height = (int) (dm.heightPixels * 0.5);

        Button btn_discard = findViewById(R.id.btn_discard);
        btn_discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //클립보드 가져오기.
        ClipData.Item item = mClipboard.getPrimaryClip().getItemAt(0);
        pasteData = item.getText().toString();
        if(pasteData == null){
            text_url.setText("url : ");
        }else {
            text_url.setText("url : " + pasteData);
        }

    }
}