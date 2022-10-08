package com.intertive.wheelview.model;

import com.intertive.wheelview.wheel.IWheelEntity;

/**
 * @author Nevio
 * on 2022/5/13
 */
public class WheelDate implements IWheelEntity {


    private int num;
    private String textCn;
    private String textEn;
    private boolean isEn;

    @Override
    public String getWheelText() {
        if (isEn){
            return textEn;
        } else {
            return textCn;
        }
    }

    public WheelDate() {
    }

    public WheelDate(int num) {
        this.num = num;
    }

    public WheelDate(int num, String nameCn) {
        this.num = num;
        this.textCn = nameCn;
    }

    public WheelDate(int num, String nameCn, String nameEn) {
        this.num = num;
        this.textCn = nameCn;
        this.textEn = nameEn;
    }

    public boolean isEn() {
        return isEn;
    }

    public void setEn(boolean en) {
        isEn = en;
    }

    public String getTextEn() {
        return textEn;
    }

    public void setTextEn(String textEn) {
        this.textEn = textEn;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getTextCn() {
        return textCn;
    }

    public void setTextCn(String textCn) {
        this.textCn = textCn;
    }

    @Override
    public String toString() {
        return "WheelDate{" +
                "num=" + num +
                ", textCn='" + textCn + '\'' +
                ", textEn='" + textEn + '\'' +
                ", isEn=" + isEn +
                '}';
    }
}
