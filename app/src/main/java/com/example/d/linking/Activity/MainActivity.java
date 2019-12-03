package com.example.d.linking.Activity;

import com.example.d.linking.Data.LoginData;
import com.example.d.linking.Data.LoginResponse;
import com.example.d.linking.Server.APIClient;
import com.example.d.linking.Server.APIInterface;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.d.linking.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 9001;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    GoogleSignInClient mGoogleSignInClient;
    private SignInButton googlelogin;
    private APIInterface service ;
    String account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //email 가져오기
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        //구글 로그인 클라이언트 객체 초기화
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googlelogin = (SignInButton)findViewById(R.id.googlelogin);
        service= APIClient.getClient().create(APIInterface.class);

        googlelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("정보", "connected to Google Play");

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("정보", "Connection Failed");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN) {
            //로그인 intent 꺼진 후에 로그인 결과 처리.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void  handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {
            Log.d("정보", "Sign In Result: Failed Code = " + e.getStatusCode());
            updateUI(null);
        }

    }
    //account.getEmail, account.getId, account.getIdToken  -> 사용자 정보 가져오기.
    private void updateUI(@Nullable GoogleSignInAccount account) {
        if(account != null) {
            this.account = account.getDisplayName();
            startLogin(new LoginData(account.getEmail(),account.getDisplayName()));
        }else {
            Toast.makeText(getApplicationContext(), "email does not exist.", Toast.LENGTH_LONG).show();
        }
    }

    // Login api connection
    private void startLogin(LoginData data) {
        service.userLogin(data).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.d("받아온결과",""+new Gson().toJson(response.body()));
                Toast.makeText(MainActivity.this, "환영합니다 "+account +"님", Toast.LENGTH_SHORT).show();
                //SharedPreferences user 정보 저장
                preferences = getSharedPreferences("user", MODE_PRIVATE);
                editor = preferences.edit();
                editor.putString("display_name",account);
                editor.commit();
                Intent intent1 = new Intent(getApplicationContext(), Workspace.class);
                startActivity(intent1);
                finish();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "로그인 error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
