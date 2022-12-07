package com.intertive.componentlib;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.intertive.http.parser.DataParser;
import com.intertive.x5web.WebViewPlugin;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import rxhttp.wrapper.param.RxHttp;

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
            startActivity(new Intent(getBaseContext(), TestActivity.class));
        });

        findViewById(R.id.btn_2).setOnClickListener(v -> {
            startActivity(new Intent(getBaseContext(), WebActivity.class));

        });
    }
}