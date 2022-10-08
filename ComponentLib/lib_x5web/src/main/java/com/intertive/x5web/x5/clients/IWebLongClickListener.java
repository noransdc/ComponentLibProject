package com.intertive.x5web.x5.clients;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.intertive.x5web.WebViewPlugin;
import com.intertive.x5web.utils.ToastX5;
import com.intertive.x5web.x5.GameWebView;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.tencent.smtt.sdk.WebView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 创建时间：2019/2/11
 * 方法编写人：Rea.X
 * 功能描述：
 */
public class IWebLongClickListener implements View.OnLongClickListener {
    private GameWebView gameWebView;
    public IWebLongClickListener(GameWebView webView) {
        gameWebView = webView;
    }

    @Override
    public boolean onLongClick(final View view) {
        WebView.HitTestResult hitTestResult = gameWebView.getHitTestResult();
        if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                hitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
            final String pic = hitTestResult.getExtra();//获取图片路径
            Acp.getInstance(view.getContext()).request(new AcpOptions.Builder().
                    setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).
                    setDeniedMessage("当前应用缺少拨打内存卡使用权限，不能保存图片。\n\n请点击\"设置\"-\"权限\"-打开所需权限。\n\n最后点击两次后退按钮，即可返回。").
                    setRationalMessage("保存屏幕截图需要获取内存卡的使用权限，请允许！").
                    build(), new AcpListener() {
                @Override
                public void onGranted() {
                    ToastX5.toastSuccess("正在保存图片...请稍等");
                    Glide.with(view.getContext()).asBitmap().load(pic).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            boolean flag = saveImage(resource);
                            if(flag){
                                ToastX5.toastSuccess("图片已保存至相册");
                            }else{
                                ToastX5.toastError("图片保存失败，请重试");
                            }
                        }
                    });
                }

                @Override
                public void onDenied(List<String> permissions) {
                    ToastX5.toastError("图片保存失败，请重试");
                }
            });
            return true;
        }
        return false;
    }


    //保存文件到指定路径
    private boolean saveImage(Bitmap bmp) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "image";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            boolean mkdir = appDir.mkdir();
            Log.e("mkdir", mkdir + "");
        }
        String fileName = System.currentTimeMillis() + ".jpg";

        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            MediaStore.Images.Media.insertImage(WebViewPlugin.getInstance().getApplication().getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            LocalBroadcastManager.getInstance(WebViewPlugin.getInstance().getApplication()).sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            return isSuccess;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
