package com.lcb.one.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.lcb.one.R;
import com.lcb.one.adapter.DemoSwipeAdapter;
import com.lcb.one.base.BaseActivity;
import com.lcb.one.bean.SwipeDate;

import java.util.ArrayList;
import java.util.List;

import yuan.kuo.yu.view.YRecyclerView;

/**
 * Description: 通过YRecycleView实现上加载下刷新加侧滑删除
 * AUTHOR: Champion Dragon
 * created at 2019/2/28
 **/
public class RefreshLoadDelete extends BaseActivity {
    private List<SwipeDate> list = new ArrayList<>();
    private YRecyclerView ycl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadmore_refresh);
        ycl = (YRecyclerView) findViewById(R.id.ycl);
        initData();
    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            SwipeDate swipeDate = new SwipeDate();
            swipeDate.name = "于" + i;
            swipeDate.type = 1;
            list.add(swipeDate);
        }
        final DemoSwipeAdapter demoAdapter = new DemoSwipeAdapter(list);
        ycl.setLayoutManager(new LinearLayoutManager(this));
        ycl.setAdapter(demoAdapter);
        ycl.setRefreshAndLoadMoreListener(new YRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        demoAdapter.addReFreshData();
                        ycl.setReFreshComplete();
                    }
                }, 2500);
            }

            @Override
            public void onLoadMore() {
                Log.i("加载更多", "000");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        demoAdapter.addRLoadMOreData();
                        ycl.setloadMoreComplete();
                    }
                }, 2500);
            }
        });
    }
}
