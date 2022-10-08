package com.intertive.wheelview.ex;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;

import androidx.annotation.Nullable;

import com.intertive.wheelview.R;
import com.intertive.wheelview.model.WheelDate;
import com.intertive.wheelview.wheel.WheelView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 日 WheelView
 *
 * @author zyyoona7
 * @version v1.0.0
 * @since 2018/8/20.
 */
public class DayWheelView extends WheelView<WheelDate> {

    private final SparseArray<List<WheelDate>> DAYS = new SparseArray<>(1);
    private int mYear;
    private int mMonth;
    private Calendar mCalendar;
    private boolean isEn;


    public DayWheelView(Context context) {
        this(context, null);
    }

    public DayWheelView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayWheelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCalendar = Calendar.getInstance();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DayWheelView);
        mYear = typedArray.getInt(R.styleable.DayWheelView_wv_year, mCalendar.get(Calendar.YEAR));
        mMonth = typedArray.getInt(R.styleable.DayWheelView_wv_month, mCalendar.get(Calendar.MONTH) + 1);
        int selectedDay = typedArray.getInt(R.styleable.DayWheelView_wv_selectedDay, mCalendar.get(Calendar.DATE));
        typedArray.recycle();
        updateDay();
        setSelectedDay(selectedDay);
    }

    public void setEn(boolean state){
        this.isEn = state;
        updateDay();
//        setSelectedDay(selectedDay);
    }

    /**
     * 同时设置年份和月份
     *
     * @param year  年份
     * @param month 月份
     */
    public void setYearAndMonth(int year, int month) {
        mYear = year;
        mMonth = month;
        updateDay();
    }

    /**
     * 设置年份
     *
     * @param year 年份
     */
    public void setYear(int year) {
        mYear = year;
        updateDay();
    }

    /**
     * 获取年份
     *
     * @return 年份
     */
    public int getYear() {
        return mYear;
    }

    /**
     * 设置月份
     *
     * @param month 月份
     */
    public void setMonth(int month) {
        mMonth = month;
        updateDay();
    }

    /**
     * 获取月份
     *
     * @return 月份
     */
    public int getMonth() {
        return mMonth;
    }

    /**
     * 更新数据
     */
    public void updateDay() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.MONTH, mMonth - 1);
        mCalendar.set(Calendar.DATE, 1);
        if (mYear == currentYear && mMonth == currentMonth) {
            mCalendar.set(Calendar.DATE, currentDay);
        } else {
            mCalendar.roll(Calendar.DATE, -1);
        }

        try {
            int days = mCalendar.get(Calendar.DATE);
            List<WheelDate> list = DAYS.get(days);
            if (list == null) {
                list = new ArrayList<>(1);
                for (int i = 1; i <= days; i++) {
                    WheelDate item = new WheelDate(i, i + "日", String.valueOf(i));
                    item.setEn(isEn);
                    list.add(item);
                }
                DAYS.put(days, list);
            } else {
                for (WheelDate item : list) {
                    item.setEn(isEn);
                }
            }
            super.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取选中的日
     *
     * @return 选中的日
     */
    public WheelDate getSelectedDay() {
        return getSelectedItemData();
    }

    /**
     * 设置选中的日
     *
     * @param selectedDay 选中的日
     */
    public void setSelectedDay(int selectedDay) {
        setSelectedDay(selectedDay, false);
    }

    /**
     * 设置选中的日
     *
     * @param selectedDay    选中的日
     * @param isSmoothScroll 是否平滑滚动
     */
    public void setSelectedDay(int selectedDay, boolean isSmoothScroll) {
        setSelectedDay(selectedDay, isSmoothScroll, 0);
    }

    /**
     * 设置选中的日
     *
     * @param selectedDay    选中的日
     * @param isSmoothScroll 是否平滑滚动
     * @param smoothDuration 平滑滚动持续时间
     */
    public void setSelectedDay(int selectedDay, boolean isSmoothScroll, int smoothDuration) {
        int days = mCalendar.get(Calendar.DATE);
        if (selectedDay >= 1 && selectedDay <= days) {
            updateSelectedDay(selectedDay, isSmoothScroll, smoothDuration);
        }
    }

    /**
     * 更新选中的日
     *
     * @param selectedDay    选中的日
     * @param isSmoothScroll 是否平滑滚动
     * @param smoothDuration 平滑滚动持续时间
     */
    private void updateSelectedDay(int selectedDay, boolean isSmoothScroll, int smoothDuration) {
        setSelectedItemPosition(selectedDay - 1, isSmoothScroll, smoothDuration);
    }

    @Override
    public void setData(List<WheelDate> dataList) {
        throw new UnsupportedOperationException("You can not invoke setData method in " + DayWheelView.class.getSimpleName() + ".");
    }
}
