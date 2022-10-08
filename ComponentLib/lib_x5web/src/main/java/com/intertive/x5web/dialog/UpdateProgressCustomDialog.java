package com.intertive.x5web.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.intertive.x5web.R;
import com.intertive.x5web.download.DownLoad;
import com.intertive.x5web.numberprogressbar.NumberProgressBar;


public class UpdateProgressCustomDialog extends AbstractUpdateProgressCustomDialog implements View.OnClickListener {

    private NumberProgressBar mProgressBar;

    public static UpdateProgressCustomDialog newInstance() {
        return new UpdateProgressCustomDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lib_x5_num_progressbar, null);
        if (getDialog().getWindow() != null){
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        initView(view);
        return view;
    }

    @Override
    public void downloadProgress(int progress) {
        if (mProgressBar != null)
            mProgressBar.setProgress(progress);
    }

    private void initView(View view) {
        mProgressBar = view.findViewById(R.id.number_progress);
        TextView cancelTv = view.findViewById(R.id.number_progress_cancle);
        cancelTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.number_progress_cancle) {
            DownLoad.INSTANT.cancelDownload();
            dismissAllowingStateLoss();
        }
    }

}
