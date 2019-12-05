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
import com.example.d.linking.R;
import com.example.d.linking.Server.APIClient;
import com.example.d.linking.Server.APIInterface;
import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Directory_Rename extends Activity {
    private APIInterface service ;
    private SharedPreferences preferences;
    Button btn_renamediscard, btn_rename;
    EditText dir_renametext;
    String display_name;
    String dir_name;
    int dir_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀 바 삭제.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dirrename);

        service= APIClient.getClient().create(APIInterface.class);

        preferences = getSharedPreferences("user",MODE_PRIVATE);
        display_name = preferences.getString("display_name","");

        Intent intent = getIntent();
        dir_id = intent.getIntExtra("dirID",0);
        dir_name = intent.getStringExtra("dirName");

        dir_renametext = (EditText)findViewById(R.id.dir_renametext);
        dir_renametext.setText(dir_name);

        btn_renamediscard = (Button)findViewById(R.id.btn_renameDiscard);
        btn_renamediscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_rename = (Button)findViewById(R.id.btn_rename);
        btn_rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DirRename(new DirAddData(dir_renametext.getText().toString()));
            }
        });
    }

    public void DirRename(DirAddData data){
        service.dirrename(display_name, dir_id, data).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("디렉토리 수정 결과",""+new Gson().toJson(response.code()));
                Toast.makeText(Directory_Rename.this, "디렉토리 이름 수정이 완료되었습니다.",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Directory_Rename.this, Workspace.class);
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
