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
import android.widget.EditText;
import android.widget.TextView;

public class LinkSave_Popup2 extends Activity {
    Button btn_discard2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀 바 삭제.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_linksave_popup2);

        btn_discard2 = (Button) findViewById(R.id.btn_discard);


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
    }
}
