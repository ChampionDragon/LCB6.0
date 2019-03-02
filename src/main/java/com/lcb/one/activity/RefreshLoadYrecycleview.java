package com.lcb.one.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.lcb.one.R;
import com.lcb.one.adapter.DemoAdapter;
import com.lcb.one.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import yuan.kuo.yu.view.YRecyclerView;

/**
 * Description: 通过YRecycleView实现上加载下刷新
 * AUTHOR: Champion Dragon
 * created at 2019/2/28
 **/
public class RefreshLoadYrecycleview extends BaseActivity {
    private List<String> list = new ArrayList<>();
    private YRecyclerView ycl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_load_yrecycleview);
        ycl = (YRecyclerView) findViewById(R.id.ycl);
        initData();
    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            list.add("于" + i);
        }
        final DemoAdapter demoAdapter = new DemoAdapter(list);
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
