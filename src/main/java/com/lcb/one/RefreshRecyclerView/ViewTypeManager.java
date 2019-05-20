package com.lcb.one.RefreshRecyclerView;

import android.util.SparseIntArray;
import android.view.ViewGroup;

/**
 * Description:为避免反射，ViewType 交给开发者自己管理(IViewTypeFactory)
 * AUTHOR: Champion Dragon
 * created at 2019/5/5
 **/
public class ViewTypeManager {
    // position to Type
    private SparseIntArray mPositionToTypeMap;

    private IViewHolderFactory mViewHolderFactory;

    public ViewTypeManager() {
        mPositionToTypeMap = new SparseIntArray();
    }

    protected int getViewType(int position) {
        return mPositionToTypeMap.get(position);
    }

    protected void putViewType(int position, int viewType) {
        mPositionToTypeMap.put(position, viewType);
    }

    public void setViewHolderFactory(IViewHolderFactory factory) {
        mViewHolderFactory = factory;
    }

    protected <T extends BaseViewHolder> T getViewHolder(ViewGroup parent, int viewType) {
        if (mViewHolderFactory != null) {
            return mViewHolderFactory.getViewHolder(parent, viewType);
        } else {
            return null;
        }
    }
}
