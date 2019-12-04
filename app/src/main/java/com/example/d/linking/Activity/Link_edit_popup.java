package com.example.d.linking.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.d.linking.Data.LinkEditData;
import com.example.d.linking.R;
import com.example.d.linking.Server.APIClient;
import com.example.d.linking.Server.APIInterface;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Link_edit_popup extends Activity {
    private APIInterface service;
    private Button btn_linkedit, btn_linkeditDiscard;
    private EditText link_editag, link_editdesc;
    String tag, desc;
    int link_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀 바 삭제.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_linkedit);

        service= APIClient.getClient().create(APIInterface.class);
        Intent intent = getIntent();
        link_id = intent.getIntExtra("link_id",0);
        tag = intent.getStringExtra("tag");
        desc = intent.getStringExtra("desc");

        btn_linkedit = (Button) findViewById(R.id.btn_linkedit);
        btn_linkeditDiscard = (Button) findViewById(R.id.btn_linkeditDiscard);
        link_editag = (EditText) findViewById(R.id.link_editag);
        link_editdesc = (EditText) findViewById(R.id.link_editdesc);

        link_editag.setText(tag);
        link_editdesc.setText(desc);

        btn_linkeditDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_linkedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                link_edit(link_id, new LinkEditData(link_editag.getText().toString(), link_editdesc.getText().toString()));
            }
        });
    }

    private void link_edit(int link_id, LinkEditData data){
        service.linkupdate(link_id, data).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(Link_edit_popup.this, "Modification completed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Link_edit_popup.this, Workspace.class);
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
