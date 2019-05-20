package com.lcb.one.bean;

/**
 * Description:卡片的bean类
 * AUTHOR: Champion Dragon
 * created at 2019/5/6
 **/
public class CardRecord {
    private String carnum;
    private String time;
    private String place;
    private String reason;
    private int price;
    private int deduct;

    public CardRecord(String carnum, String time, String place, String reason, int price, int deduct) {
        this.carnum = carnum;
        this.time = time;
        this.place = place;
        this.reason = reason;
        this.price = price;
        this.deduct = deduct;
    }

    public String getCarnum() {

        return carnum;
    }

    public void setCarnum(String carnum) {
        this.carnum = carnum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDeduct() {
        return deduct;
    }

    public void setDeduct(int deduct) {
        this.deduct = deduct;
    }


}

