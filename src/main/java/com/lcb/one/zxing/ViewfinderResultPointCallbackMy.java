package com.lcb.one.zxing;

import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import com.lcb.one.zxing.main.MyViewfinderView;

public final class ViewfinderResultPointCallbackMy implements ResultPointCallback {

    private final MyViewfinderView viewfinderView;

    public ViewfinderResultPointCallbackMy(MyViewfinderView viewfinderView) {
        this.viewfinderView = viewfinderView;
    }

    public void foundPossibleResultPoint(ResultPoint point) {
        viewfinderView.addPossibleResultPoint(point);
    }

}