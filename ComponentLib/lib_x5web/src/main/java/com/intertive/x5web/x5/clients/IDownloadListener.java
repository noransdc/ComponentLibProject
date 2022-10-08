package com.intertive.x5web.x5.clients;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.intertive.x5web.WebViewPlugin;
import com.intertive.x5web.utils.ToastX5;
import com.intertive.x5web.x5.tools.DownloadAndInstallApk;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.tencent.smtt.sdk.DownloadListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


/**
 * author: Rea.X
 * date: 2017/10/13.
 */

public class IDownloadListener implements DownloadListener {
    private Context context;

    public IDownloadListener(Context context) {
        this.context = context;
    }

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long length) {
        WebViewPlugin.wvLog("onDownloadStart:::::url:" + url);
        WebViewPlugin.wvLog("onDownloadStart:::::userAgent:" + userAgent);
        WebViewPlugin.wvLog("onDownloadStart:::::contentDisposition:" + contentDisposition);
        WebViewPlugin.wvLog("onDownloadStart:::::mimeType:" + mimeType);
        WebViewPlugin.wvLog("onDownloadStart:::::length:" + length);
        if (url.startsWith("data:")) {
            Acp.getInstance(context).request(new AcpOptions.Builder().
                    setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).
                    setDeniedMessage("当前应用缺少拨打内存卡使用权限，不能保存文件。\n\n请点击\"设置\"-\"权限\"-打开所需权限。\n\n最后点击两次后退按钮，即可返回。").
                    setRationalMessage("保存文件需要获取内存卡的使用权限，请允许！").
                    build(), new AcpListener() {
                @Override
                public void onGranted() {
                    createAndSaveFileFromBase64Url(url);
                }

                @Override
                public void onDenied(List<String> permissions) {
                    ToastX5.toastError("文件保存失败，请重试");
                }
            });
            return;
        }
        if (mimeType.equals("application/vnd.android.package-archive")) {
            DownloadAndInstallApk.INSTANT.downloadApkAndInstall(context, url);
        } else {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                context.startActivity(intent);
            } catch (Exception e) {
                ToastX5.toastError("无法下载文件");
            }
        }
    }


    private Handler handler;
    private void createAndSaveFileFromBase64Url(final String url) {
        ToastX5.toastError("正在保存，请稍等...");
        handler = new Handler(Looper.getMainLooper());
        new Thread() {
            @Override
            public void run() {
                super.run();
                String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "image";
                File path = new File(storePath);
                String filetype = url.substring(url.indexOf("/") + 1, url.indexOf(";"));
                final String filename = System.currentTimeMillis() + "." + filetype;
                final File file = new File(path, filename);
                try {
                    if(!path.exists())
                        path.mkdirs();

                    String base64EncodedString = url.substring(url.indexOf(",") + 1);
                    byte[] decodedBytes = Base64.decode(base64EncodedString, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                    bitmap = createBitmap(bitmap);
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
//                    OutputStream os = new FileOutputStream(file);
//                    os.write(decodedBytes);
//                    os.flush();
//                    os.close();

                    handler.post(() -> {
                        //把文件插入到系统图库
                        try {
                            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), filename, null);
                            //保存图片后发送广播通知更新数据库
                            Uri uri = Uri.fromFile(file);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                            ToastX5.toastError("文件保存成功，位置："+file.getAbsolutePath());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

                } catch (IOException e) {
                    handler.post(() -> ToastX5.toastError("文件保存失败"));
                }
            }
        }.start();
    }

    /**
     * 解决原始Bitmap是透明的，保存在本地就是黑色的问题
     */
    private Bitmap createBitmap(Bitmap originBitmap) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        Bitmap.Config config = originBitmap.getConfig();
        Bitmap bitmap = Bitmap.createBitmap(originBitmap.getWidth(),
                originBitmap.getHeight(), config);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0, 0, originBitmap.getWidth(), originBitmap.getHeight(), paint);
        canvas.drawBitmap(originBitmap, 0, 0, paint);
        return bitmap;
    }
}

