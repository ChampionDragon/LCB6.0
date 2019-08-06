package com.lcb.one.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lcb.one.R;
import com.lcb.one.RefreshRecyclerView.BaseViewHolder;
import com.lcb.one.util.Logs;

/**
 * Description:图片的ViewHolder
 * AUTHOR: Champion Dragon
 * created at 2019/5/7
 **/
public class ImageViewHolder extends BaseViewHolder<String> {

    private ImageView mImage;

    public ImageViewHolder(ViewGroup parent) {
        super(parent, R.layout.holder_image);
    }

    @Override
    public void onInitializeView() {
        super.onInitializeView();
        mImage = findViewById(R.id.image);
    }

    @Override
    public void setData(String object) {
        super.setData(object);
        Glide.with(itemView.getContext())
                .load(object)
                .error(R.mipmap.imgerror)
                .into(mImage);
    }

    @Override
    public void onItemViewClick(String object) {
        super.onItemViewClick(object);
        Logs.i("ImageViewHolder44:"+" 当前位置:"+getAdapterPosition());
    }
}