package com.lcb.one.RefreshRecyclerView;

import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:view复用管理者
 * AUTHOR: Champion Dragon
 * created at 2019/5/5 0005
 **/
public class ViewHolderManager {

    private final String TAG = "ViewHolderManager";
    private int mViewType = 10;
    private Map<Class<? extends BaseViewHolder>, Integer> mHolderToTypeMap;
    private SparseArray<Class<? extends BaseViewHolder>> mTypeToHolderMap;
    //position to ViewType
    private SparseIntArray mPositionToTypeMap;

    public ViewHolderManager() {
        mHolderToTypeMap = new HashMap<>();
        mTypeToHolderMap = new SparseArray<>();
        mPositionToTypeMap = new SparseIntArray();
    }

    public void putViewType(int position, int type){
        mPositionToTypeMap.put(position, type);
    }

    public int getViewType(int position) {
        return mPositionToTypeMap.get(position);
    }

    public void addViewHolder(Class<? extends BaseViewHolder> viewHolder) {
        if (!mHolderToTypeMap.containsKey(viewHolder)) {
            //获取ViewHolder的泛型数据class
            Class dataClass = (Class) ((ParameterizedType) viewHolder.getGenericSuperclass()).getActualTypeArguments()[0];
            mViewType ++;
            mHolderToTypeMap.put(viewHolder, mViewType);
            mTypeToHolderMap.put(mViewType,viewHolder);
            Log.i("lcb", TAG+"46  addViewHolder dataClassType : " + dataClass.getName());
        }
    }

    public int getViewType(Class<? extends BaseViewHolder> holder){
        if(!mHolderToTypeMap.containsKey(holder)){
            throw new IllegalArgumentException("please invoke add ViewHolder method");
        }
        return mHolderToTypeMap.get(holder);
    }

    public Class<? extends BaseViewHolder> getViewHolderClass(int viewType){
        if(mTypeToHolderMap.get(viewType) == null){
            throw new IllegalArgumentException("please invoke add ViewHolder method");
        }
        return mTypeToHolderMap.get(viewType);
    }
}

