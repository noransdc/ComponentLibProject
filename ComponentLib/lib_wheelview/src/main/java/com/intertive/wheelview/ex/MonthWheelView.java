package com.intertive.wheelview.ex;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.intertive.wheelview.R;
import com.intertive.wheelview.WheelUtil;
import com.intertive.wheelview.model.WheelDate;
import com.intertive.wheelview.wheel.WheelView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 月 WheelView
 *
 * @author zyyoona7
 * @version v1.0.0
 * @since 2018/8/20.
 */
public class MonthWheelView extends WheelView<WheelDate> {

    private int startMonth = 1;
    private int endMonth = 12;
    private boolean isEn;


    public MonthWheelView(Context context) {
        this(context, null);
    }

    public MonthWheelView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonthWheelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MonthWheelView);
        int selectedMonth = typedArray.getInt(R.styleable.MonthWheelView_wv_selectedMonth, Calendar.getInstance().get(Calendar.MONTH) + 1);
        typedArray.recycle();
        initData();
        setSelectedMonth(selectedMonth);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        List<WheelDate> list = new ArrayList<>(1);
        for (int i = startMonth; i <= endMonth; i++) {
            WheelDate item = new WheelDate(i, i + "月", WheelUtil.getMonthEn(i));
            item.setEn(isEn);
            list.add(item);
        }
        super.setData(list);
    }

    public void setEn(boolean state){
        this.isEn = state;
        initData();
    }

    /**
     * 设置月份范围
     */
    public void setMonthRange(int startMonth, int endMonth){
        this.startMonth = startMonth;
        this.endMonth = endMonth;
        initData();
    }

    /**
     * 获取选中的月
     *
     * @return 选中的月
     */
    public WheelDate getSelectedMonth() {
        return getSelectedItemData();
    }

    /**
     * 设置选中的月
     *
     * @param selectedMonth 选中的月
     */
    public void setSelectedMonth(int selectedMonth) {
        setSelectedMonth(selectedMonth, false);
    }

    /**
     * 设置选中的月
     *
     * @param selectedMonth  选中的月
     * @param isSmoothScroll 是否平滑滚动
     */
    public void setSelectedMonth(int selectedMonth, boolean isSmoothScroll) {
        setSelectedMonth(selectedMonth, isSmoothScroll, 0);
    }

    /**
     * 设置选中的月
     *
     * @param selectedMonth  选中的月
     * @param isSmoothScroll 是否平滑滚动
     * @param smoothDuration 平滑滚动持续时间
     */
    public void setSelectedMonth(int selectedMonth, boolean isSmoothScroll, int smoothDuration) {
        if (selectedMonth >= 1 && selectedMonth <= 12) {
            updateSelectedMonth(selectedMonth, isSmoothScroll, smoothDuration);
        }
    }

    /**
     * 更新选中的月份
     *
     * @param selectedMonth  选中的月份
     * @param isSmoothScroll 是否平滑滚动
     * @param smoothDuration 平滑滚动持续时间
     */
    private void updateSelectedMonth(int selectedMonth, boolean isSmoothScroll, int smoothDuration) {
        setSelectedItemPosition(selectedMonth - 1, isSmoothScroll, smoothDuration);
    }

    @Override
    public void setData(List<WheelDate> dataList) {
        throw new UnsupportedOperationException("You can not invoke setData method in " + MonthWheelView.class.getSimpleName() + ".");
    }
}
