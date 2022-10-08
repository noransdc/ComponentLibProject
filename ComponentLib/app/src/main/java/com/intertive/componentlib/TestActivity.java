package com.intertive.componentlib;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nevio
 * on 2022/2/4
 */
public class TestActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

//        String url = ""
//        Glide.with(this).load(url).

        initViewPager();

    }

    private void test(){

    }


    private void initViewPager(){
        SlidingTabLayout tabLayout = findViewById(R.id.stl_tab_layout);
        ViewPager viewPager = findViewById(R.id.vp_view_pager);


        List<String> titleList = new ArrayList<>();
        titleList.add("title1");
        titleList.add("titleList2");
        titleList.add("titleList title3");

//        String[] titleArr = new String[]{"title1", "title2", "title3"};

        List<Fragment> list = new ArrayList<>();
        list.add(new TestFragment1());
        list.add(new TestFragment1());
        list.add(new TestFragment1());

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

        tabLayout.setViewPager(viewPager, titleList);

    }


}
