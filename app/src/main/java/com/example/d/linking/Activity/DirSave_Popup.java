package com.example.d.linking.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.d.linking.Data.DirAddData;
import com.example.d.linking.Data.LinkAddResponse;
import com.example.d.linking.R;
import com.example.d.linking.Server.APIClient;
import com.example.d.linking.Server.APIInterface;
import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DirSave_Popup extends Activity {
    private APIInterface service ;
    private SharedPreferences preferences;
    Button btn_dirDiscard, btn_dirSave;
    EditText dir_name;
    String display_name;
    int dir_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀 바 삭제.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dirsave);

        service= APIClient.getClient().create(APIInterface.class);

        preferences = getSharedPreferences("user",MODE_PRIVATE);
        display_name = preferences.getString("display_name","");

        Intent intent = getIntent();
        dir_id = intent.getIntExtra("dirID",0);

        //배경 제거
        WindowManager.LayoutParams  layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags  = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount  = 0.8f;
        getWindow().setAttributes(layoutParams);

        //dialog size 설정
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        getWindow().getAttributes().width = (int) (dm.widthPixels * 0.9);
        getWindow().getAttributes().height = (int) (dm.heightPixels * 0.25);

        dir_name = (EditText)findViewById(R.id.dir_name);

        btn_dirDiscard = (Button)findViewById(R.id.btn_dirDiscard);
        btn_dirDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_dirSave = (Button)findViewById(R.id.btn_dirSave);
        btn_dirSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DirAdd(new DirAddData(dir_name.getText().toString()));
            }
        });
    }

    private  void  DirAdd(DirAddData data){
        service.diradd(display_name, dir_id, data).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(DirSave_Popup.this, "save success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DirSave_Popup.this, Workspace.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }
}
