package com.example.d.linking.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.d.linking.Data.LinkEditData;
import com.example.d.linking.R;
import com.example.d.linking.Server.APIClient;
import com.example.d.linking.Server.APIInterface;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Link_edit_popup extends Activity {
    private APIInterface service;
    private Button btn_linkedit, btn_linkeditDiscard;
    private EditText link_editag, link_editdesc;
    String tag, desc;
    private ImageButton btn_add;
    ListView list_tag;
    int link_id;
    private ArrayList<String> items;
    private ArrayAdapter adapter;

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
        btn_add = (ImageButton) findViewById(R.id.btn_add);
        list_tag = (ListView) findViewById(R.id.list_tag);

        link_editag.setText(tag);
        link_editdesc.setText(desc);

        btn_linkeditDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        items = new ArrayList<String>() ;
        adapter = new ArrayAdapter(this, R.layout.item_tag, R.id.text_tag,items) ;
        list_tag.setAdapter(adapter);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = link_editag.getText().toString();
                items.add(str);
                adapter.notifyDataSetChanged();
                link_editag.setText("");
            }
        });

        list_tag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                adapter.notifyDataSetChanged();
            }
        });

        btn_linkedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                link_edit(link_id, new LinkEditData(items, link_editdesc.getText().toString()));
            }
        });
    }

    private void link_edit(int link_id, LinkEditData data){
        service.linkupdate(link_id, data).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(Link_edit_popup.this, "링크 수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
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
