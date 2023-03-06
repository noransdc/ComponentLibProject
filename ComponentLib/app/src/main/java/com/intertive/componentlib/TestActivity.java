package com.intertive.componentlib;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.intertive.http.parser.DataParser;
import com.intertive.tab.InterTabLayout;
import com.intertive.tab.InterTabTitle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import rxhttp.wrapper.param.RxHttp;

/**
 * @author Nevio
 * on 2022/2/4
 */
public class TestActivity extends AppCompatActivity {


    private InterTabLayout tabLayout;
    private ViewPager viewPager;
    private Button clickBtn;


    @Override
    protected void onCreate(@Nullable  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

//        String url = ""
//        Glide.with(this).load(url).


        tabLayout = findViewById(R.id.stl_tab_layout);
        viewPager = findViewById(R.id.vp_view_pager);
        clickBtn = findViewById(R.id.btn_click);





        initViewPager();


        clickBtn.setOnClickListener(v -> {
            List<InterTabTitle> titleList = new ArrayList<>();

            Random random = new Random();
            for (int i = 0; i < 7; i++) {
                titleList.add(new InterTabTitle("title" + random.nextInt(10), random.nextFloat() + ""));
            }

            Log.w("tag",  titleList.toString());
            tabLayout.updateTitleText(titleList);
        });

        findViewById(R.id.btn_tab).setOnClickListener(v -> {
//            tabLayout.setIndicatorDrawable(getDrawable(R.drawable.ia_corner_orange_fe5e00_22));


            test();
        });

    }

    private void test(){
        RxHttp.get("http://13.212.18.206:5222/test/test/getUrl")
                .addHeader("pch", "xiaoenlaile")
                .add("username", "JJBX")
                .asParser(new DataParser<String>(){})
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    Log.w("http success", data);
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.w("http failed", throwable.getMessage());
                    }
                });

    }


    private void initViewPager(){



        List<InterTabTitle> titleList = new ArrayList<>();
        titleList.add(new InterTabTitle("title1", "a"));
        titleList.add(new InterTabTitle("title2", "ab"));
        titleList.add(new InterTabTitle("title3", "abc"));
        titleList.add(new InterTabTitle("title4", "abcd"));
        titleList.add(new InterTabTitle("title5", "a"));
        titleList.add(new InterTabTitle("title6", "ab"));
        titleList.add(new InterTabTitle("title7", "a"));

//        String[] titleArr = new String[]{"title1", "title2", "title3"};

        List<Fragment> list = new ArrayList<>();
        for (int i = 0; i < titleList.size(); i++) {
            list.add(new TestFragment1());
        }

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

            @Override
            public int getCount() {
                return list.size();
            }
        });

        tabLayout.setViewPagerTab(viewPager, titleList);

        tabLayout.setCurrentTab(2);
    }


}
