package com.example.d.linking.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.d.linking.R;
import com.example.d.linking.Server.APIClient;
import com.example.d.linking.Server.APIInterface;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserSetting extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private APIInterface service;
    String display_name;
    Button btn_userdelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usersetting);
        //display_name 가져오기.
        preferences = getSharedPreferences("user", MODE_PRIVATE);
        display_name = preferences.getString("display_name","");

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setTitle(Html.fromHtml("<font color='#2c3130'>"+display_name+"</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //현재 google 로그인 가져오기.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        service= APIClient.getClient().create(APIInterface.class);

        btn_userdelete = (Button) findViewById(R.id.btn_userdelete);
        btn_userdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.userdelete(display_name).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Toast.makeText(UserSetting.this, "Withdrawal Completed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UserSetting.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mGoogleSignInClient.signOut();
                        editor = preferences.edit();
                        editor.putString("display_name"," ");
                        editor.putInt("dir_id",0);
                        editor.putString("dir_name"," ");
                        editor.commit();
                        startActivity(intent);
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                    }
                });
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return true;
    }

    //logout
    public void logout(View v) {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast logoutToast = Toast.makeText(getApplicationContext(), "Logout completed.", Toast.LENGTH_SHORT);
                        logoutToast.show();
                        Intent intent = new Intent(UserSetting.this, MainActivity.class);
                        editor = preferences.edit();
                        editor.putString("display_name"," ");
                        editor.putInt("dir_id",0);
                        editor.putString("dir_name"," ");
                        editor.commit();
                        startActivity(intent);
                        finish();
                    }
                });
    }
}
