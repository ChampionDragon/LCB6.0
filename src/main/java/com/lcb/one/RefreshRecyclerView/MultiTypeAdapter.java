package com.lcb.one.RefreshRecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import com.lcb.one.util.Logs;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

/**
 * Description:复杂的数据类型列表 Adapter , 没有 Header , Footer 的概念，所有的 item 都对应一个ViewHolder
 * 通过反射自动处理 onCreateViewHolder 过程，如需避免反射调用请使用 CustomMultiTypeAdapter
 * 注意：通过反射实现，支持 ViewHolder 的带有一个参数（ViewGroup）和无参两种形式构造函数，性能方面微小的损耗。
 * 构造函数为保证反射时能获取到，应该写成 public 静态内部类 或者 public 的单独类。
 * AUTHOR: Champion Dragon
 * created at 2019/5/5
 **/
public class MultiTypeAdapter extends RecyclerAdapter {

    private final String TAG = "MultiTypeAdapter";
    private ViewHolderManager mViewHolderManager;

    public MultiTypeAdapter(Context context) {
        super(context);
        mViewHolderManager = new ViewHolderManager();
    }

    @Override
    public int getItemViewType(int position) {
        if (hasEndStatusView() && position == mViewCount - 1) {
            return STATUS_TYPE;
        }
        return mViewHolderManager.getViewType(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Logs.i(TAG + "41:onCreateViewHolder -- viewType : " + viewType);
        if (mViewHolderManager == null) {
            throw new ExceptionInInitializerError("mViewHolderManager is null , it need init");
        }
        if (viewType == STATUS_TYPE) {
            return new BaseViewHolder(mStatusView);
        }
        Class clazzViewHolder = mViewHolderManager.getViewHolderClass(viewType);
        try {
            //这里只适配了 ViewHolder 构造函数只有 ViewGroup.class 参数 或者 无参 情况的构造函数，具体请看 Demo
            BaseViewHolder holder;
            Constructor constructor = clazzViewHolder.getDeclaredConstructor(new Class[]{ViewGroup.class});
            constructor.setAccessible(true);
            holder = (BaseViewHolder) constructor.newInstance(new Object[]{parent});
            if (holder == null) {
                constructor = clazzViewHolder.getDeclaredConstructor();
                holder = (BaseViewHolder) constructor.newInstance();
            }
            return holder;
        } catch (Exception e) {
            Log.e("lcb", TAG + "57: " + e.getMessage());
        }
        return null;
    }

    @Override
    public BaseViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        Logs.v(TAG + ":73viewCount : " + mViewCount + " position : " + position);
        if (position < mData.size()) {
            holder.setData(mData.get(position));
        }
        // 显示加载更多
        if (!mIsNoMoring && mLoadMoreEnable && !mIsLoadMoring && isValidLoadMore(position)) {
            performLoadMore();
        }
    }

    public <T> void add(Class<? extends BaseViewHolder<T>> viewHolder, T data) {
        if (mIsNoMoring || data == null || viewHolder == null) {
            return;
        }
        mIsLoadMoring = false;
        mData.add(data);
        mViewHolderManager.addViewHolder(viewHolder);
        int viewType = mViewHolderManager.getViewType(viewHolder);

        int positionStart;

        if (hasEndStatusView()) {
            positionStart = mViewCount - 1;
        } else {
            positionStart = mViewCount;
        }
        if (positionStart >= 0) {
            mViewHolderManager.putViewType(positionStart, viewType);
            mViewCount++;
            notifyItemRangeInserted(positionStart, 1);
        }
    }

    public <T> void addAll(Class<? extends BaseViewHolder<T>> viewHolder, T[] data) {
        addAll(viewHolder, Arrays.asList(data));
    }

    public <T> void addAll(Class<? extends BaseViewHolder<T>> viewHolder, List<T> data) {
        if (mIsNoMoring || data == null || data.size() == 0) {
            return;
        }
        mIsLoadMoring = false;
        int size = data.size();
        mData.addAll(data);
        mViewHolderManager.addViewHolder(viewHolder);
        int viewType = mViewHolderManager.getViewType(viewHolder);

        int positionStart;
        if (hasEndStatusView()) {
            positionStart = mViewCount - 1;
        } else {
            positionStart = mViewCount;
        }
        if (positionStart >= 0) {
            for (int i = 0; i < size; i++) {
                mViewHolderManager.putViewType(positionStart + i, viewType);
            }
            mViewCount += size;
            notifyItemRangeInserted(positionStart, size);
        }
    }
}
