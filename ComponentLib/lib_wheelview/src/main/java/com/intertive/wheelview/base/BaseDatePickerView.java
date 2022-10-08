package com.intertive.wheelview.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.intertive.wheelview.WheelUtil;
import com.intertive.wheelview.ex.DayWheelView;
import com.intertive.wheelview.ex.MonthWheelView;
import com.intertive.wheelview.ex.YearWheelView;
import com.intertive.wheelview.listener.OnDateChooseListener;
import com.intertive.wheelview.listener.OnDateSelectedListener;
import com.intertive.wheelview.listener.OnPickerScrollStateChangedListener;
import com.intertive.wheelview.model.WheelDate;
import com.intertive.wheelview.wheel.WheelView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class BaseDatePickerView extends FrameLayout
        implements WheelView.OnItemSelectedListener<WheelDate>, WheelView.OnWheelChangedListener {

    private final SimpleDateFormat mYmdSdf;
    private final SimpleDateFormat mYmSdf;
    protected YearWheelView mYearWv;
    protected MonthWheelView mMonthWv;
    protected DayWheelView mDayWv;

    private OnDateSelectedListener mOnDateSelectedListener;
    private OnPickerScrollStateChangedListener mOnPickerScrollStateChangedListener;
    private OnDateChooseListener onDateChooseListener;

    public BaseDatePickerView(@NonNull Context context) {
        this(context, null);
    }

    public BaseDatePickerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseDatePickerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mYmdSdf = new SimpleDateFormat("yyyy-M-d", Locale.getDefault());
        mYmSdf = new SimpleDateFormat("yyyy-M", Locale.getDefault());
        if (getDatePickerViewLayoutId() != 0) {
            LayoutInflater.from(context).inflate(getDatePickerViewLayoutId(), this);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int yearId = getYearWheelViewId();
        if (!isNoId(yearId)) {
            mYearWv = findViewById(yearId);
        }
        int monthId = getMonthWheelViewId();
        if (!isNoId(monthId)) {
            mMonthWv = findViewById(monthId);
        }
        int dayId = getDayWheelViewId();
        if (!isNoId(dayId)) {
            mDayWv = findViewById(dayId);
        }
        if (mYearWv != null) {
            mYearWv.setOnItemSelectedListener(this);
            mYearWv.setOnWheelChangedListener(this);
        }
        if (mMonthWv != null) {
            mMonthWv.setOnItemSelectedListener(this);
            mMonthWv.setOnWheelChangedListener(this);
        }
        if (mDayWv != null) {
            mDayWv.setOnItemSelectedListener(this);
            mDayWv.setOnWheelChangedListener(this);
        }
    }

    /**
     * 获取 datePickerView 的布局资源id
     *
     * @return 布局资源id
     */
    @LayoutRes
    protected abstract int getDatePickerViewLayoutId();

    /**
     * 获取 YearWheelView 的 id
     *
     * @return YearWheelView id
     */
    @IdRes
    protected abstract int getYearWheelViewId();

    /**
     * 获取 MonthWheelView 的 id
     *
     * @return MonthWheelView id
     */
    @IdRes
    protected abstract int getMonthWheelViewId();

    /**
     * 获取 DayWheelView id
     *
     * @return DayWheelView id
     */
    @IdRes
    protected abstract int getDayWheelViewId();

    @Override
    public void onItemSelected(WheelView<WheelDate> wheelView, WheelDate data, int position) {
        if (wheelView.getId() == getYearWheelViewId()) {
            //年份选中
            if (mDayWv != null) {
                mDayWv.setYear(data.getNum());
            }
        } else if (wheelView.getId() == getMonthWheelViewId()) {
            //月份选中
            if (mDayWv != null) {
                mDayWv.setMonth(data.getNum());
            }
        }

        try {
            WheelDate selectYear, selectMonth, selectDay;
            if (mYearWv == null || mYearWv.getSelectedYear() == null) {
                selectYear = new WheelDate(1970, 1970 + "年", String.valueOf(1970));
            } else {
                selectYear = mYearWv.getSelectedYear();
            }
            if (mMonthWv == null || mMonthWv.getSelectedMonth() == null) {
                selectMonth = new WheelDate(1, 1 + "月", WheelUtil.getMonthEn(1));
            } else {
                selectMonth = mMonthWv.getSelectedMonth();
            }
            if (mDayWv == null || mDayWv.getSelectedDay() == null) {
                selectDay = new WheelDate(1, 1 + "日", String.valueOf(1));
            } else {
                selectDay = mDayWv.getSelectedDay();
            }

            if (onDateChooseListener != null) {
                onDateChooseListener.onDateSelected(this, selectYear, selectMonth, selectDay);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onWheelScroll(int scrollOffsetY) {

    }

    @Override
    public void onWheelItemChanged(int oldPosition, int newPosition) {

    }

    @Override
    public void onWheelSelected(int position) {

    }

    @Override
    public void onWheelScrollStateChanged(int state) {
        if (mOnPickerScrollStateChangedListener != null) {
            mOnPickerScrollStateChangedListener.onScrollStateChanged(state);
        }
    }

    private boolean isAllShown() {
        return isYmShown()
                && mDayWv != null && mDayWv.getVisibility() == VISIBLE;
    }

    private boolean isYmShown() {
        return mYearWv != null && mYearWv.getVisibility() == VISIBLE
                && mMonthWv != null && mMonthWv.getVisibility() == VISIBLE;
    }

    private boolean isNoId(@IdRes int idRes) {
        return idRes == 0 || idRes == NO_ID;
    }

    /**
     * 获取年份 WheelView
     *
     * @return 年份 WheelView
     */
    public YearWheelView getYearWv() {
        return mYearWv;
    }

    /**
     * 获取月份 WheelView
     *
     * @return 月份 WheelView
     */
    public MonthWheelView getMonthWv() {
        return mMonthWv;
    }

    /**
     * 获取日 WheelView
     *
     * @return 日 WheelView
     */
    public DayWheelView getDayWv() {
        return mDayWv;
    }


    /**
     * 设置日期回调监听器
     *
     * @param onDateSelectedListener 日期回调监听器
     */
    public void setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
        mOnDateSelectedListener = onDateSelectedListener;
    }

    /**
     * 设置滚动状态变化监听
     *
     * @param listener 滚动状态变化监听器
     */
    public void setOnPickerScrollStateChangedListener(OnPickerScrollStateChangedListener listener) {
        mOnPickerScrollStateChangedListener = listener;
    }


    public void setOnDateChooseListener(OnDateChooseListener listener) {
        this.onDateChooseListener = listener;
    }

}
