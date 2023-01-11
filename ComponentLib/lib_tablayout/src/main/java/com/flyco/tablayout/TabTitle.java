package com.flyco.tablayout;

/**
 * @author Nevio
 * on 2022/10/20
 */
public class TabTitle {

    private String id;
    private String title1;
    private String title2;

    public TabTitle() {
    }

    public TabTitle(String title1) {
        this.title1 = title1;
    }

    public TabTitle(String title1, String title2) {
        this.title1 = title1;
        this.title2 = title2;
    }

    public TabTitle(String id, String title1, String title2) {
        this.id = id;
        this.title1 = title1;
        this.title2 = title2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle1() {
        return title1;
    }

    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    @Override
    public String toString() {
        return "TabTitle{" +
                "id='" + id + '\'' +
                ", title1='" + title1 + '\'' +
                ", title2='" + title2 + '\'' +
                '}';
    }
}
