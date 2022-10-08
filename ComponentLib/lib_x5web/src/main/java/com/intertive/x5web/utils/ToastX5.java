package com.intertive.x5web.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;

import com.intertive.x5web.R;
import com.intertive.x5web.cache.CacheManager;


/**
 * Created by Rea.X on 2017/2/2.
 * <p>Toast工具类</p>
 */

public class ToastX5 {

    private static boolean isShowFace = false;
    private static ToastConfig toastConfig = new ToastConfig() {
        @Override
        public Toast providerToast(String message, int res) {
            return super.providerToast(message, res);
        }
    };

    public static void toastError(String msg) {
        show(msg, R.drawable.lib_x5_toast_error_tips);
    }


    public static void toastSuccess(String msg) {
        show(msg, R.drawable.lib_x5_toast_success_tips);
    }

    public static void toastWarn(String msg) {
        show(msg, R.drawable.lib_x5_toast_error_tips);
    }

    public static boolean isIsShowFace() {
        return isShowFace;
    }

    public static void setIsShowFace(boolean isShowFace) {
        ToastX5.isShowFace = isShowFace;
    }

    public static void setIToastConfig(ToastConfig toastConfig){
        ToastX5.toastConfig = toastConfig;
    }

    private static void show(String message, @DrawableRes int res) {
        Toast toast = toastConfig.providerToast(message, res);
        toast.show();
    }

    private static abstract class ToastConfig{

        public Toast providerToast(String message, @DrawableRes int res) {
            return defaultToast(message, res);
        }

        private Toast defaultToast(String message, @DrawableRes int res) {
            Context context = CacheManager.getApplication();
            if (ToastX5.isIsShowFace()) {
                View view = View.inflate(context, R.layout.lib_x5_error_dialog, null);
                TextView textView = view.findViewById(R.id.show_content);
                if (!TextUtils.isEmpty(message)) {
                    textView.setText(message);
                }
                ImageView imageView = view.findViewById(R.id.show_img);
                imageView.setImageResource(res);
                Toast toast = new Toast(context);
                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 70);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(view);
                return toast;
            } else {
                TextView textView = new TextView(context);
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                textView.setBackgroundResource(R.drawable.lib_x5_toast_bg_black);
                textView.setPadding(120, 20, 120, 20);
                if (!TextUtils.isEmpty(message)) {
                    textView.setText(message);
                }
                Toast toast = new Toast(context);
                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(textView);
                return toast;
            }
        }
    }

}
