package com.intertive.banner.helper;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Conrad
 * on 2019/7/8
 */
public class ScrollSpeedLinearLayoutManager extends LinearLayoutManager {

    private Context context;
    private float MILLISECONDS_PER_INCH = 0.4f;

    public ScrollSpeedLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        this.context = context;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {

        LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {

            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return ScrollSpeedLinearLayoutManager.this.computeScrollVectorForPosition(targetPosition);
            }

            //This returns the milliseconds it takes to //scroll one pixel.
            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return MILLISECONDS_PER_INCH / displayMetrics.density;
                //返回滑动一个pixel需要多少毫秒
            }
        };
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }

    public void setSpeedSlow() {
        //自己在这里用density去乘，希望不同分辨率设备上滑动速度相同
        // 0.3f是自己估摸的一个值，可以根据不同需求自己修改
        MILLISECONDS_PER_INCH = context.getResources().getDisplayMetrics().density * 0.3f;
    }

    public void setSpeedFast() {
        MILLISECONDS_PER_INCH = context.getResources().getDisplayMetrics().density * 0.03f;
    }

}
