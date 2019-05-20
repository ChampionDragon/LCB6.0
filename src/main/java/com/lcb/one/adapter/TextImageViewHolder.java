package com.lcb.one.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcb.one.R;
import com.lcb.one.RefreshRecyclerView.BaseViewHolder;
import com.lcb.one.bean.TextImage;
import com.lcb.one.util.Logs;

/**
 * Description:文字图片的ViewHolder
 * AUTHOR: Champion Dragon
 * created at 2019/5/7
 **/
public class TextImageViewHolder extends BaseViewHolder<TextImage> {

    private TextView mText;
    private ImageView mImage;

    public TextImageViewHolder(ViewGroup parent) {
        super(parent, R.layout.holder_text_image);
    }

    @Override
    public void onInitializeView() {
        super.onInitializeView();
        mText = findViewById(R.id.txtimg_text);
        mImage = findViewById(R.id.txtimg_image);
    }

    @Override
    public void setData(TextImage object) {
        super.setData(object);
        mText.setText(object.text);
        Glide.with(itemView.getContext())
                .load(object.image)
                .error(R.mipmap.imgerror)
                .into(mImage);
    }

    @Override
    public void onItemViewClick(TextImage data) {
        super.onItemViewClick(data);
        Logs.i("TextImageViewHolder47:" + data + " 当前位置:" + getAdapterPosition());
    }
}
