package com.intertive.wheelview.model;

import java.util.List;

/**
 * @author Nevio
 * on 2022/2/10
 */
public class WheelArea {

    private String code;
    private String name;
    private List<WheelArea> list;

    public WheelArea() {
    }

    public WheelArea(String code, String name, List<WheelArea> list) {
        this.code = code;
        this.name = name;
        this.list = list;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WheelArea> getList() {
        return list;
    }

    public void setList(List<WheelArea> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "AreaInfo{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", list=" + list +
                '}';
    }
}
