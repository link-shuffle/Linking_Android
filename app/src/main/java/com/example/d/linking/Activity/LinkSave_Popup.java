package com.example.d.linking.Activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.d.linking.Data.LinkAddData;
import com.example.d.linking.Data.LinkAddResponse;
import com.example.d.linking.R;
import com.example.d.linking.Server.APIClient;
import com.example.d.linking.Server.APIInterface;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//extend Activity -> to fullscreen
public class LinkSave_Popup extends Activity{
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Button btn_discard, btn_save;
    private ImageButton btn_add2;
    private TextView text_url;
    private EditText tag, desc;
    private ListView list_tag;
    private ClipboardManager mClipboard;
    private String pasteData, display_name;
    private APIInterface service ;
    private ArrayList<String> items;
    private ArrayAdapter adapter;
    int dir_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀 바 삭제.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_linksave_popup);
        preferences = getSharedPreferences("user",MODE_PRIVATE);
        display_name = preferences.getString("display_name","");
        Intent intent = getIntent();
        dir_id = intent.getIntExtra("diradd_id",0);


        btn_discard = (Button) findViewById(R.id.btn_discard2);
        btn_save = (Button) findViewById(R.id.btn_save2);
        text_url = (TextView) findViewById(R.id.text_url);
        list_tag = (ListView) findViewById(R.id.list_tag2);
        btn_add2 = (ImageButton) findViewById(R.id.btn_add2);
        tag = (EditText) findViewById(R.id.edit_tag);
        desc = (EditText) findViewById(R.id.edit_des);
        mClipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        pasteData = "";

        btn_discard = findViewById(R.id.btn_discard2);
        btn_discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = preferences.edit();
                editor.putString("URL",text_url.getText().toString());
                editor.commit();
                finish();
            }
        });

        //클립보드 가져오기.
        try{
            ClipData.Item item = mClipboard.getPrimaryClip().getItemAt(0);
            pasteData = item.getText().toString();
            text_url.setText(pasteData);
            btn_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    linkAdd(new LinkAddData(text_url.getText().toString(), items, desc.getText().toString()));
                }
            });
        }catch (NullPointerException e){

        }

        items = new ArrayList<String>() ;
        adapter = new ArrayAdapter(this, R.layout.item_tag, R.id.text_tag,items) ;
        list_tag.setAdapter(adapter);

        service= APIClient.getClient().create(APIInterface.class);

        btn_add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list_tag.getCount() >= 6) {
                    Toast.makeText(LinkSave_Popup.this, "최대 5개의 태그가 입력 가능합니다.", Toast.LENGTH_SHORT).show();
                }else {
                    String str = tag.getText().toString();
                    items.add(str);
                    adapter.notifyDataSetChanged();
                    tag.setText("");
                }
            }
        });

        list_tag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                adapter.notifyDataSetChanged();
            }
        });

        list_tag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
    }

    // linkadd
    private void linkAdd(LinkAddData data) {
        service.linkadd(dir_id,display_name,data).enqueue(new Callback<LinkAddResponse>() {
            @Override
            public void onResponse(Call<LinkAddResponse> call, Response<LinkAddResponse> response) {
                Toast.makeText(LinkSave_Popup.this, "링크 저장이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                Log.d("링크 저장 결과",""+new Gson().toJson(response.code()));
                editor = preferences.edit();
                editor.putString("URL",text_url.getText().toString());
                editor.commit();

                Intent intent = new Intent(LinkSave_Popup.this, Workspace.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
            @Override
            public void onFailure(Call<LinkAddResponse> call, Throwable t) {
            }
        });
    }
}