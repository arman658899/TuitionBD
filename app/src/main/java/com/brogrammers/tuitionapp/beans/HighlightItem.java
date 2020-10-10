package com.brogrammers.tuitionapp.beans;

public class HighlightItem {
    String tittle, des, childKey, buttonName;

    public HighlightItem(String tittle, String des, String childKey, String buttonName) {
        this.tittle = tittle;
        this.des = des;
        this.childKey = childKey;
        this.buttonName = buttonName;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getChildKey() {
        return childKey;
    }

    public void setChildKey(String childKey) {
        this.childKey = childKey;
    }

    public String getButtonName() {
        return buttonName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }
}
