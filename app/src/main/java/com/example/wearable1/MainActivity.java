package com.example.wearable1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bt_cls = findViewById(R.id.btn_cls);
        bt_cls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { go_classification(); }
        });

        Button bt_res = findViewById(R.id.btn_res);
        bt_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { go_respiration(); }
        });
    }

    private void go_classification(){
        Intent go_cls = new Intent(MainActivity.this, Classification_Activity.class);
        startActivity(go_cls);
    }

    private void go_respiration(){
        Intent go_res = new Intent(MainActivity.this, Respiration_Activity.class);
        startActivity(go_res);
    }
}