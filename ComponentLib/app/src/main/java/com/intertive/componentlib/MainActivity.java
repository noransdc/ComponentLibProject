package com.intertive.componentlib;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.intertive.x5web.WebViewPlugin;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        test1();


        WebViewPlugin.getInstance().init(getApplication(), new WebViewConfig());



    }

    private void test1(){
        findViewById(R.id.btn_1).setOnClickListener(v -> {
            startActivity(new Intent(getBaseContext(), Test2Activity.class));
        });

        findViewById(R.id.btn_2).setOnClickListener(v -> {
            startActivity(new Intent(getBaseContext(), WebActivity.class));

        });
    }
}