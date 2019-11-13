package com.example.d.linking;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class WorkSpace extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //link save popup test
        //startActivity(new Intent(this, LinkSave_Popup.class));

        //Navigation drawer test
        startActivity(new Intent(this,User_workspace.class));
        finish();
    }
}
