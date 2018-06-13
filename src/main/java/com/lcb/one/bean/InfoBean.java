package com.lcb.one.bean;

/**
 * Description:数据的bean类
 * AUTHOR: Champion Dragon
 * created at 2017/10/20
 **/

public class InfoBean {
    String key = "";
    String value = "";

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public InfoBean(String key, String value) {
        this.key = key;
        this.value = value;
    }


}
