package com.intertive.banner.listener;

import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by Sai on 15/7/29.
 * 翻页指示器适配器
 */
public class CBBannerPageChangeListener implements OnBannerPageChangeListener {
    private ArrayList<ImageView> pointViews;
    private int[] page_indicatorId;
    private OnBannerPageChangeListener onBannerPageChangeListener;
    public CBBannerPageChangeListener(ArrayList<ImageView> pointViews, int page_indicatorId[]){
        this.pointViews=pointViews;
        this.page_indicatorId = page_indicatorId;
    }

    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if(onBannerPageChangeListener != null) onBannerPageChangeListener.onScrollStateChanged(recyclerView, newState);
    }

    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if(onBannerPageChangeListener != null) onBannerPageChangeListener.onScrolled(recyclerView,dx,dy);
    }

    public void onPageSelected(int index) {
        for (int i = 0; i < pointViews.size(); i++) {
            pointViews.get(index).setImageResource(page_indicatorId[1]);
            if (index != i) {
                pointViews.get(i).setImageResource(page_indicatorId[0]);
            }
        }
        if(onBannerPageChangeListener != null) onBannerPageChangeListener.onPageSelected(index);

    }

    public void setOnPageChangeListener(OnBannerPageChangeListener onBannerPageChangeListener) {
        this.onBannerPageChangeListener = onBannerPageChangeListener;
    }
}
