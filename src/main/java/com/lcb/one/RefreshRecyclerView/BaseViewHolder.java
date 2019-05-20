package com.lcb.one.RefreshRecyclerView;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Description:顶级父类
 * AUTHOR: Champion Dragon
 * created at 2019/5/5
 **/
public class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    private final String TAG = "BaseViewHolder";
    private T mData;

    public BaseViewHolder(ViewGroup parent, int layoutId) {
        this(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
    }

    public BaseViewHolder(View itemView) {
        super(itemView);
        onInitializeView();
        //设置短按监听
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemViewClick(mData);
            }
        });
        //设置长按监听
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemLongClick(mData);
                return true;//设置ture可以抢短按的焦点，不然的话长按的同时也会触发短按事件
            }
        });
    }


    public void onInitializeView() {

    }

    public <T extends View> T findViewById(@IdRes int resId) {
        if (itemView != null) {
            return (T) itemView.findViewById(resId);
        } else {
            return null;
        }
    }

    public void setData(final T data) {
        if (data == null) {
            return;
        }
        mData = data;
    }

    public T getData() {
        return mData;
    }

    /*短按监听*/
    public void onItemViewClick(T data) {

    }

    /*长按监听*/
    public void onItemLongClick(T mData) {
    }


}
