package com.lcb.one.base;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description: 创建线程池,最多四条线程同时开启，防止程序OOM
 * AUTHOR: Champion Dragon
 * created at 2019/5/29
 **/
public class AsyncTaskExecutor {
    private static AsyncTaskExecutor mInstance = null;
    private static final int maxExecutor = 4;
    private ExecutorService mExecutor = null;

    public static AsyncTaskExecutor getinstance() {
        if (mInstance == null) {
            mInstance = new AsyncTaskExecutor();
        }
        return mInstance;
    }

    private AsyncTaskExecutor() {
        mExecutor = Executors.newFixedThreadPool(maxExecutor);
    }

    public void submit(Runnable a) {
        mExecutor.execute(a);
    }

}