package com.lcb.one.bean;

/**
 * Description:RecyclerView监听的数据对象
 * AUTHOR: Champion Dragon
 * created at 2019/5/7
 **/
public class Rvdata {
    private int position;//item在RecyclerView所在的位置编号

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Rvdata(int position) {

        this.position = position;
    }
}
