package com.lcb.one.RefreshRecyclerView;

import android.view.ViewGroup;

/**
 * Description:为避免反射，ViewType 交给开发者自己管理
 * AUTHOR: Champion Dragon
 * created at 2019/5/5
 **/
public interface IViewHolderFactory {
    <V extends BaseViewHolder> V getViewHolder(ViewGroup parent, int viewType);
}
