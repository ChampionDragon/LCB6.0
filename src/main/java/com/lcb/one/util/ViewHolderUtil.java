package com.lcb.one.util;

import android.util.SparseArray;
import android.view.View;

/**
 * Description: view复用的工具类
 * AUTHOR: Champion Dragon
 * created at 2018/4/25
 **/

public class ViewHolderUtil {
    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHoler = (SparseArray<View>) view.getTag();
        if (viewHoler == null) {
            viewHoler = new SparseArray<>();
            view.setTag(viewHoler);
        }
        View childrenView = viewHoler.get(id);
        if (childrenView == null) {
            childrenView = view.findViewById(id);
            viewHoler.put(id, childrenView);
        }
        return (T) childrenView;
    }
}
