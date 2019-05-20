package com.lcb.one.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.lcb.one.RefreshRecyclerView.BaseViewHolder;
import com.lcb.one.RefreshRecyclerView.RecyclerAdapter;
import com.lcb.one.bean.CardRecord;
import com.lcb.one.listener.RvListener;
import com.lcb.one.util.Logs;

/**
 * Description:卡片的适配器
 * AUTHOR: Champion Dragon
 * created at 2019/5/6
 **/
public class CardRecordAdapter extends RecyclerAdapter<CardRecord> {
private RvListener rvListener;
    public CardRecordAdapter(Context context) {
        super(context);
    }
    public CardRecordAdapter(Context context, RvListener listener) {
        this(context);
        rvListener = listener;
    }

    @Override
    public BaseViewHolder<CardRecord> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        if (rvListener!=null) {
            Logs.v(rvListener+" ");
            return new CardRecordHolder(parent,rvListener);
        }
        return new CardRecordHolder(parent);
    }
}