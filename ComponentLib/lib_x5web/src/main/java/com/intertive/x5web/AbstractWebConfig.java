package com.intertive.x5web;


import com.intertive.x5web.dialog.AbstractUpdateProgressCustomDialog;
import com.intertive.x5web.dialog.UpdateProgressCustomDialog;

public abstract class AbstractWebConfig {
    public enum RequestType{
        GET,
        POST;
    }
    /**
     * 判断是否是调试模式
     */
    public abstract boolean isDebug();

    /**
     * 加载中是否显示线条状的进度条
     *
     * @return true:显示
     */
    public abstract boolean isShowLineLoading();

    /**
     * 是否显示圆形进度条
     *
     * @return true:显示
     */
    public abstract boolean isShowCircleLoading();

    /**
     * 手机站域名
     *
     * @return 域名列表
     */
    public String[] getDomains(){
        return null;
    }

    /**
     * 获取CDN域名
     * @return CDN域名
     */
    public String getCDNHost(){
        return null;
    }

    /**
     * 项目中必须注册FileProvided
     *
     * @return FileProvided Auth
     */
    public abstract String fileProvidedAuth();

    /**
     * 自定义下载进度弹框
     *
     * @return 下载进度dialog
     */
    public AbstractUpdateProgressCustomDialog downloadApkProgressDialog() {
        return new UpdateProgressCustomDialog();
    }

    /**
     * 用户数据传送到手机站的方式，GET或者POST
     * @return 参数传递方式
     */
    public RequestType userParamsTransMethod(){
        return RequestType.GET;
    }

    /**
     * 是否允许缓存静态资源
     * @return
     */
    public boolean allowCacheResource(){
        return true;
    }
}
