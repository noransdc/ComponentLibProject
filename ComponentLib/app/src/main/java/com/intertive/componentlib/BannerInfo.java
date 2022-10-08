package com.intertive.componentlib;

/**
 * @author Nevio
 * on 2022/3/14
 */
public class BannerInfo {



    private String imageUrl;
    private int imgRes;

    public BannerInfo() {
    }

    public BannerInfo(int imgRes) {
        this.imgRes = imgRes;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }

    @Override
    public String toString() {
        return "BannerInfo{" +
                "imageUrl='" + imageUrl + '\'' +
                ", imgRes=" + imgRes +
                '}';
    }
}
