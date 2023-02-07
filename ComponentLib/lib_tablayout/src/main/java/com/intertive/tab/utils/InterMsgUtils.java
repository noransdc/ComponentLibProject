package com.intertive.tab.utils;


import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;

import com.intertive.tab.widget.InterMsgView;

/**
 * 未读消息提示View,显示小红点或者带有数字的红点:
 * 数字一位,圆
 * 数字两位,圆角矩形,圆角是高度的一半
 * 数字超过两位,显示99+
 */
public class InterMsgUtils {
    public static void show(InterMsgView interMsgView, int num) {
        if (interMsgView == null) {
            return;
        }
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) interMsgView.getLayoutParams();
        DisplayMetrics dm = interMsgView.getResources().getDisplayMetrics();
        interMsgView.setVisibility(View.VISIBLE);
        if (num <= 0) {//圆点,设置默认宽高
            interMsgView.setStrokeWidth(0);
            interMsgView.setText("");

            lp.width = (int) (5 * dm.density);
            lp.height = (int) (5 * dm.density);
            interMsgView.setLayoutParams(lp);
        } else {
            lp.height = (int) (18 * dm.density);
            if (num > 0 && num < 10) {//圆
                lp.width = (int) (18 * dm.density);
                interMsgView.setText(num + "");
            } else if (num > 9 && num < 100) {//圆角矩形,圆角是高度的一半,设置默认padding
                lp.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                interMsgView.setPadding((int) (6 * dm.density), 0, (int) (6 * dm.density), 0);
                interMsgView.setText(num + "");
            } else {//数字超过两位,显示99+
                lp.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                interMsgView.setPadding((int) (6 * dm.density), 0, (int) (6 * dm.density), 0);
                interMsgView.setText("99+");
            }
            interMsgView.setLayoutParams(lp);
        }
    }

    public static void setSize(InterMsgView rtv, int size) {
        if (rtv == null) {
            return;
        }
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) rtv.getLayoutParams();
        lp.width = size;
        lp.height = size;
        rtv.setLayoutParams(lp);
    }
}
