package com.intertive.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.intertive.banner.adapter.CBPageAdapter;
import com.intertive.banner.helper.CBLoopScaleHelper;
import com.intertive.banner.helper.ScrollSpeedLinearLayoutManager;
import com.intertive.banner.holder.CBViewHolderCreator;
import com.intertive.banner.listener.CBBannerPageChangeListener;
import com.intertive.banner.listener.OnBannerItemClickListener;
import com.intertive.banner.listener.OnBannerPageChangeListener;
import com.intertive.banner.view.CBLoopViewPager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * 页面翻转控件，极方便的广告栏
 * 支持无限循环，自动翻页，翻页特效
 *
 * @author Sai 支持自动翻页
 */
public class ConvenientBanner<T> extends RelativeLayout {
    private List<T> mDatas;
    private int[] page_indicatorId;
    private ArrayList<ImageView> mPointViews = new ArrayList<>();
    private CBPageAdapter pageAdapter;
    private CBLoopViewPager viewPager;
    private ViewGroup loPageTurningPoint;
    private long autoTurningTime = -1;
    private boolean turning;
    private boolean canTurn = false;
    private boolean canLoop = true;
    private CBLoopScaleHelper cbLoopScaleHelper;
    private CBBannerPageChangeListener pageChangeListener;
    private OnBannerPageChangeListener onBannerPageChangeListener;
    private AdSwitchTask adSwitchTask;
    private boolean isVertical = false;

    public enum PageIndicatorAlign {
        ALIGN_PARENT_LEFT, ALIGN_PARENT_RIGHT, CENTER_HORIZONTAL
    }

    public ConvenientBanner(Context context) {
        super(context);
        init(context);
    }

    public ConvenientBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ConvenientBanner);
        canLoop = a.getBoolean(R.styleable.ConvenientBanner_canLoop, true);
        autoTurningTime = a.getInteger(R.styleable.ConvenientBanner_autoTurningTime, -1);
        a.recycle();
        init(context);
    }

    private void init(Context context) {
        View hView = LayoutInflater.from(context).inflate(R.layout.include_viewpager, this, true);
        viewPager = hView.findViewById(R.id.cbLoopViewPager);
        loPageTurningPoint = hView.findViewById(R.id.loPageTurningPoint);
        final LinearLayoutManager linearLayoutManager = new ScrollSpeedLinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false);
        viewPager.setLayoutManager(linearLayoutManager);
        cbLoopScaleHelper = new CBLoopScaleHelper();
        adSwitchTask = new AdSwitchTask(this);
    }

    public ConvenientBanner setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        viewPager.setLayoutManager(layoutManager);
        return this;
    }

    public ConvenientBanner setPages(CBViewHolderCreator holderCreator, List<T> datas) {
        this.mDatas = datas;
        pageAdapter = new CBPageAdapter(holderCreator, mDatas, canLoop);
        viewPager.setAdapter(pageAdapter);

        if (page_indicatorId != null)
            setPageIndicator(page_indicatorId);

        cbLoopScaleHelper.setFirstItemPos(canLoop ? mDatas.size() : 0);
        cbLoopScaleHelper.attachToRecyclerView(viewPager);

        return this;
    }

    public ConvenientBanner setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
        pageAdapter.setCanLoop(canLoop);
        notifyDataSetChanged();
        return this;
    }

    public boolean isCanLoop() {
        return canLoop;
    }


    /**
     * 通知数据变化
     */
    public void notifyDataSetChanged() {
        viewPager.getAdapter().notifyDataSetChanged();
        if (page_indicatorId != null)
            setPageIndicator(page_indicatorId);
        cbLoopScaleHelper.setCurrentItem(canLoop ? mDatas.size() : 0);
    }

    /**
     * 设置底部指示器是否可见
     */
    public ConvenientBanner setPointViewVisible(boolean visible) {
        loPageTurningPoint.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * 底部指示器资源图片
     */
    public ConvenientBanner setPageIndicator(int[] page_indicatorId) {
        loPageTurningPoint.removeAllViews();
        mPointViews.clear();
        this.page_indicatorId = page_indicatorId;
        if (mDatas == null) return this;
        for (int count = 0; count < mDatas.size(); count++) {
            // 翻页指示的点
            ImageView pointView = new ImageView(getContext());
            if (cbLoopScaleHelper.getFirstItemPos() % mDatas.size() == count) {
                pointView.setImageResource(page_indicatorId[1]);
            } else {
                pointView.setImageResource(page_indicatorId[0]);
            }
            LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            parentParams.width = dp2px(6);
            parentParams.height = dp2px(6);
            parentParams.setMargins(0, 0, dp2px(8), 0);
            mPointViews.add(pointView);
            loPageTurningPoint.addView(pointView, parentParams);
        }
        pageChangeListener = new CBBannerPageChangeListener(mPointViews, page_indicatorId);
        cbLoopScaleHelper.setOnPageChangeListener(pageChangeListener);
        if (onBannerPageChangeListener != null){
            pageChangeListener.setOnPageChangeListener(onBannerPageChangeListener);
        }
        return this;
    }

    public OnBannerPageChangeListener getOnPageChangeListener() {
        return onBannerPageChangeListener;
    }

    /**
     * 设置翻页监听器
     */
    public ConvenientBanner setOnBannerPageChangeListener(OnBannerPageChangeListener onBannerPageChangeListener) {
        this.onBannerPageChangeListener = onBannerPageChangeListener;
        //如果有默认的监听器（即是使用了默认的翻页指示器）则把用户设置的依附到默认的上面，否则就直接设置
        if (pageChangeListener != null)
            pageChangeListener.setOnPageChangeListener(onBannerPageChangeListener);
        else cbLoopScaleHelper.setOnPageChangeListener(onBannerPageChangeListener);
        return this;
    }

    /**
     * 监听item点击
     */
    public ConvenientBanner setOnBannerItemClickListener(final OnBannerItemClickListener onBannerItemClickListener) {
        if (onBannerItemClickListener == null) {
            pageAdapter.setOnItemClickListener(null);
            return this;
        }
        pageAdapter.setOnItemClickListener(onBannerItemClickListener);
        return this;
    }

    /**
     * 获取当前页对应的position
     */
    public int getCurrentItem() {
        return cbLoopScaleHelper.getRealCurrentItem();
    }

    /**
     * 设置当前页对应的position
     */
    public ConvenientBanner setCurrentItem(int position, boolean smoothScroll) {
        cbLoopScaleHelper.setCurrentItem(canLoop ? mDatas.size() + position : position, smoothScroll);
        return this;
    }

    /**
     * 设置第一次加载当前页对应的position
     * setPageIndicator之前使用
     */
    public ConvenientBanner setFirstItemPos(int position) {
        cbLoopScaleHelper.setFirstItemPos(canLoop ? mDatas.size() + position : position);
        return this;
    }

    /**
     * 指示器的方向
     *
     * @param align 三个方向：居左 （RelativeLayout.ALIGN_PARENT_LEFT），
     *              居中 （RelativeLayout.CENTER_HORIZONTAL），
     *              居右 （RelativeLayout.ALIGN_PARENT_RIGHT）
     */
    public ConvenientBanner setPageIndicatorAlign(PageIndicatorAlign align) {
        LayoutParams layoutParams = (LayoutParams) loPageTurningPoint.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, align == PageIndicatorAlign.ALIGN_PARENT_LEFT ? RelativeLayout.TRUE : 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, align == PageIndicatorAlign.ALIGN_PARENT_RIGHT ? RelativeLayout.TRUE : 0);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, align == PageIndicatorAlign.CENTER_HORIZONTAL ? RelativeLayout.TRUE : 0);
        loPageTurningPoint.setLayoutParams(layoutParams);
        return this;
    }

    /***
     * 是否开启了翻页
     */
    public boolean isTurning() {
        return turning;
    }

    /***
     * 开始翻页
     * @param autoTurningTime 自动翻页时间
     */
    public ConvenientBanner startTurning(long autoTurningTime) {
        if (autoTurningTime < 0) return this;
        //如果是正在翻页的话先停掉
        if (turning) {
            stopTurning();
        }
        //设置可以翻页并开启翻页
        canTurn = true;
        this.autoTurningTime = autoTurningTime;
        turning = true;
        postDelayed(adSwitchTask, autoTurningTime);
        return this;
    }

    public ConvenientBanner startTurning() {
        startTurning(autoTurningTime);
        return this;
    }


    public void stopTurning() {
        turning = false;
        removeCallbacks(adSwitchTask);
    }

    //触碰控件的时候，翻页应该停止，离开的时候如果之前是开启了翻页的话则重新启动翻页

    float startX, startY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        int action = ev.getAction();
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_OUTSIDE) {
            // 开始翻页
            if (canTurn) startTurning(autoTurningTime);
        } else if (action == MotionEvent.ACTION_DOWN) {
            // 停止翻页
            if (canTurn) stopTurning();
        }

        return super.dispatchTouchEvent(ev);
    }

    static class AdSwitchTask implements Runnable {

        private final WeakReference<ConvenientBanner> reference;

        AdSwitchTask(ConvenientBanner convenientBanner) {
            this.reference = new WeakReference<ConvenientBanner>(convenientBanner);
        }

        @Override
        public void run() {
            ConvenientBanner convenientBanner = reference.get();

            if (convenientBanner != null) {
                if (convenientBanner.viewPager != null && convenientBanner.turning) {
                    int page = convenientBanner.cbLoopScaleHelper.getCurrentItem() + 1;
                    convenientBanner.cbLoopScaleHelper.setCurrentItem(page, true);
                    convenientBanner.postDelayed(convenientBanner.adSwitchTask, convenientBanner.autoTurningTime);
                }
            }
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
