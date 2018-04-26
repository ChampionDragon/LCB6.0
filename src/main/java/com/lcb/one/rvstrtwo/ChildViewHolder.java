package com.lcb.one.rvstrtwo;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.lcb.one.R;
import com.lcb.one.view.MyListView;

/**
 * Description: 子布局ViewHolder
 * AUTHOR: Champion Dragon
 * created at 2018/4/25
 **/

public class ChildViewHolder extends BaseViewHolder {


    private Context mContext;
    private View view;
    private TextView childLeftText;
    private TextView childRightText;
    private MyListView lv;

    public ChildViewHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;
        this.view = itemView;
    }

    public void bindView(final DataBean dataBean, final int pos) {

        childLeftText = (TextView) view.findViewById(R.id.child_left_text);
        childRightText = (TextView) view.findViewById(R.id.child_right_text);
        childLeftText.setText(dataBean.getChildLeftTxt());
        childRightText.setText(dataBean.getChildRightTxt());
        lv = (MyListView) view.findViewById(R.id.child_lv);
        lv.setAdapter(new StringAdapter(mContext, dataBean.getListdata()));

    }


}
