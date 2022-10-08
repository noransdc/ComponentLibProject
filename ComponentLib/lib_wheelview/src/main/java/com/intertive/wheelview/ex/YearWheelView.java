package com.intertive.wheelview.ex;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.intertive.wheelview.R;
import com.intertive.wheelview.model.WheelDate;
import com.intertive.wheelview.wheel.WheelView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 年 WheelView
 *
 * @author zyyoona7
 * @version v1.0.0
 * @since 2018/8/20.
 */
public class YearWheelView extends WheelView<WheelDate> {

    private int mStartYear;
    private int mEndYear;
    private boolean isEn;


    public YearWheelView(Context context) {
        this(context, null);
    }

    public YearWheelView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YearWheelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.YearWheelView);
        mStartYear = typedArray.getInt(R.styleable.YearWheelView_wv_startYear, 1900);
        mEndYear = typedArray.getInt(R.styleable.YearWheelView_wv_endYear, 2100);
        int selectedYear = typedArray.getInt(R.styleable.YearWheelView_wv_selectedYear, Calendar.getInstance().get(Calendar.YEAR));
        typedArray.recycle();

        updateYear();
        setSelectedYear(selectedYear);
    }

    public void setEn(boolean state){
        this.isEn = state;
        updateYear();
    }

    /**
     * 设置年份区间
     *
     * @param start 起始年份
     * @param end   结束年份
     */
    public void setYearRange(int start, int end) {
        mStartYear = start;
        mEndYear = end;
        updateYear();
    }

    /**
     * 更新年份数据
     */
    private void updateYear() {
        List<WheelDate> list = new ArrayList<>(1);
        for (int i = mStartYear; i <= mEndYear; i++) {
            WheelDate item = new WheelDate(i, i + "年", String.valueOf(i));
            item.setEn(isEn);
            list.add(item);
        }
        super.setData(list);
    }

    /**
     * 获取当前选中的年份
     *
     * @return 当前选中的年份
     */
    public WheelDate getSelectedYear() {
        return getSelectedItemData();
    }

    /**
     * 设置当前选中的年份
     *
     * @param selectedYear 选中的年份
     */
    public void setSelectedYear(int selectedYear) {
        setSelectedYear(selectedYear, false);
    }

    /**
     * 设置当前选中的年份
     *
     * @param selectedYear   选中的年份
     * @param isSmoothScroll 是否平滑滚动
     */
    public void setSelectedYear(int selectedYear, boolean isSmoothScroll) {
        setSelectedYear(selectedYear, isSmoothScroll, 0);
    }

    /**
     * 设置当前选中的年份
     *
     * @param selectedYear   选中的年份
     * @param isSmoothScroll 是否平滑滚动
     * @param smoothDuration 平滑滚动持续时间
     */
    public void setSelectedYear(int selectedYear, boolean isSmoothScroll, int smoothDuration) {
        if (selectedYear >= mStartYear && selectedYear <= mEndYear) {
            updateSelectedYear(selectedYear, isSmoothScroll, smoothDuration);
        }
    }

    /**
     * 更新选中的年份
     *
     * @param selectedYear   选中的年
     * @param isSmoothScroll 是否平滑滚动
     * @param smoothDuration 平滑滚动持续时间
     */
    private void updateSelectedYear(int selectedYear, boolean isSmoothScroll, int smoothDuration) {
        setSelectedItemPosition(selectedYear - mStartYear, isSmoothScroll, smoothDuration);
    }

    @Override
    public void setData(List<WheelDate> dataList) {
        throw new UnsupportedOperationException("You can not invoke setData method in " + YearWheelView.class.getSimpleName() + ".");
    }
}
