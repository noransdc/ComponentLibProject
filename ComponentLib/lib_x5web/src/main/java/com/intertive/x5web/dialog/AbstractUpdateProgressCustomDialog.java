package com.intertive.x5web.dialog;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.intertive.x5web.download.DownLoad;


public abstract class AbstractUpdateProgressCustomDialog extends DialogFragment {

    private boolean isShowing = false;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        isShowing = true;
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        isShowing = true;
        return super.show(transaction, tag);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        isShowing = true;
        super.show(manager, tag);
    }

    public abstract void downloadProgress(int progress);

    @Override
    public void dismiss() {
        super.dismiss();
        isShowing = false;
        DownLoad.INSTANT.cancelDownload();
        if(listener != null)
            listener.onDismiss();
    }

    @Override
    public void dismissAllowingStateLoss() {
        super.dismissAllowingStateLoss();
        isShowing = false;
        DownLoad.INSTANT.cancelDownload();
        if(listener != null)
            listener.onDismiss();
    }

    private onDismissListener listener;
    public interface onDismissListener{
        void onDismiss();
    }

    public void setOnDismissListener(onDismissListener listener){
        this.listener = listener;
    }
}
