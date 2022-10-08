package com.intertive.roundimg;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * @author Nevio
 * on 2022/2/7
 */
public class CustomImageView extends AppCompatImageView {


    public CustomImageView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public CustomImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs){

    }

}
