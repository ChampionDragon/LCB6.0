package com.lcb.one.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lcb.one.R;
import com.lcb.one.bean.InfoBean;

/**
 * Description: 信息的适配器
 * AUTHOR: Champion Dragon
 * created at 2017/10/20
 **/
public class InfoAdapter extends ArrayAdapter<InfoBean> {
    private int resourceId;
    private Context context;

    public InfoAdapter(Context context, int resource) {
        super(context, resource);
        resourceId = resource;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InfoBean info = getItem(position);
        View view = LayoutInflater.from(context).inflate(resourceId, null);
        TextView key = (TextView) view.findViewById(R.id.infoKey);
        TextView value = (TextView) view.findViewById(R.id.infoValue);
        key.setText(info.getKey());
        value.setText(info.getValue());
        return view;
    }
}