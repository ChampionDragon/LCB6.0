package com.lcb.one.adapter;

import android.view.ViewGroup;
import android.widget.TextView;

import com.lcb.one.R;
import com.lcb.one.RefreshRecyclerView.BaseViewHolder;
import com.lcb.one.bean.CardRecord;
import com.lcb.one.bean.Rvdata;
import com.lcb.one.listener.RvListener;
import com.lcb.one.util.Logs;

/**
 * Description:卡片的ViewHolder
 * AUTHOR: Champion Dragon
 * created at 2019/5/6
 **/
public class CardRecordHolder extends BaseViewHolder<CardRecord> {

    private TextView carnum;
    private TextView place;
    private TextView reason;
    private TextView price;
    private TextView deduct;
    private TextView time;
    private RvListener rvListener;

    public CardRecordHolder(ViewGroup parent) {
        super(parent, R.layout.holder_consume);
    }

    public CardRecordHolder(ViewGroup parent, RvListener listener) {
        this(parent);
        rvListener = listener;
    }

    @Override
    public void setData(final CardRecord object) {
        super.setData(object);
        carnum.setText(object.getCarnum());
        place.setText(object.getPlace());
        reason.setText(object.getReason());
        price.setText(object.getPrice() + "元");
        deduct.setText(object.getDeduct() + "分");
        time.setText(object.getTime());
    }

    @Override
    public void onInitializeView() {
        super.onInitializeView();
        carnum = findViewById(R.id.consume_carnum);
        place = findViewById(R.id.consume_palce);
        reason = findViewById(R.id.consume_reason);
        price = findViewById(R.id.consume_price);
        deduct = findViewById(R.id.consume_deduct);
        time = findViewById(R.id.consume_time);
    }

    @Override
    public void onItemViewClick(CardRecord object) {
        super.onItemViewClick(object);
        //短按事件
        Logs.i("CardRecordHolder:" + object + " 当前位置:" + getAdapterPosition());
    }


    @Override
    public void onItemLongClick(CardRecord mData) {
        super.onItemLongClick(mData);
        //长按事件
        if (rvListener!=null) {
            rvListener.setdata(new Rvdata(getAdapterPosition()));
        }
    }
}
