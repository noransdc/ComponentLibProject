package com.intertive.componentlib;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.intertive.banner.ConvenientBanner;
import com.intertive.banner.holder.CBViewHolderCreator;
import com.intertive.banner.holder.Holder;
import com.intertive.banner.listener.OnBannerPageChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nevio
 * on 2022/3/14
 */
public class Test2Activity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);




        List<BannerInfo> list = new ArrayList<>();
        list.add(new BannerInfo(R.drawable.ht_card));
        list.add(new BannerInfo(R.drawable.ht_card1));

        initBanner(list);

    }


    private void initBanner(List<BannerInfo> bannerList) {
        ConvenientBanner<Integer> mainBanner = findViewById(R.id.banner);
        if (bannerList == null) {
            return;
        }
        List<Integer> urlList = new ArrayList<>();
        for (BannerInfo bannerInfo : bannerList) {
            urlList.add(bannerInfo.getImgRes());
        }
        mainBanner
                .setPages(new CBViewHolderCreator() {
                    @Override
                    public Holder createHolder(View itemView) {
                        return new NetImageHolder(itemView);
                    }

                    @Override
                    public int getLayoutId() {
                        return R.layout.item_banner;
                    }
                }, urlList)
                .setOnBannerItemClickListener(position -> {

                });
        mainBanner.startTurning(3000);

        mainBanner.setOnBannerPageChangeListener(new OnBannerPageChangeListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }

            @Override
            public void onPageSelected(int index) {

            }
        });
    }

    private class NetImageHolder extends Holder<Integer> {

        private ImageView imageView;

        private NetImageHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void initView(View itemView) {
            imageView = itemView.findViewById(R.id.iv_item_banner_img);
        }

        @Override
        public void updateUI(Integer data) {
            try {
//                ImgUtil.load(activity, data, imageView);
                imageView.setImageResource(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
