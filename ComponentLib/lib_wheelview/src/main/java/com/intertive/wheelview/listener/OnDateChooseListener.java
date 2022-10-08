package com.intertive.wheelview.listener;

import androidx.annotation.Nullable;

import com.intertive.wheelview.base.BaseDatePickerView;
import com.intertive.wheelview.model.WheelDate;

import java.util.Date;

/**
 * @author Nevio
 * on 2022/5/13
 */
public interface OnDateChooseListener {


    /**
     * @param datePickerView BaseDatePickerView
     */
    void onDateSelected(BaseDatePickerView datePickerView, WheelDate year, WheelDate month,WheelDate day);


}
