package com.example.d.linking.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.d.linking.Data.UserData;
import com.example.d.linking.R;
import com.example.d.linking.Server.APIClient;
import com.example.d.linking.Server.APIInterface;
import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class User_Rename extends Activity {
    private APIInterface service ;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String display_name;
    private EditText user_renametext;
    private Button btn_userRename, btn_userDiscard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀 바 삭제.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_userrename);
        service= APIClient.getClient().create(APIInterface.class);

        preferences = getSharedPreferences("user",MODE_PRIVATE);
        display_name = preferences.getString("display_name","");

        user_renametext = (EditText) findViewById(R.id.user_renametext);
        btn_userDiscard = (Button) findViewById(R.id.btn_userDiscard);
        btn_userRename = (Button) findViewById(R.id.btn_userRename);
        user_renametext.setText(display_name);

        btn_userDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_userRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userUpdate(display_name, new UserData(user_renametext.getText().toString()));
            }
        });

    }

    public void userUpdate(String display_name, UserData data){
        service.userUpdate(display_name, data).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("디렉토리 수정 결과",""+new Gson().toJson(response.code()));
                Toast.makeText(User_Rename.this, "닉네임 수정이 완료되었습니다.",Toast.LENGTH_SHORT).show();
                editor = preferences.edit();
                editor.putString("display_name",user_renametext.getText().toString());
                editor.commit();
                Intent intent = new Intent(User_Rename.this, Workspace.class);
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
