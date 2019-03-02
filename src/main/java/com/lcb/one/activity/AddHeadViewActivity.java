package com.lcb.one.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.lcb.one.R;
import com.lcb.one.adapter.DemoAdapter;
import com.lcb.one.base.BaseActivity;

import java.util.ArrayList;

import yuan.kuo.yu.view.YRecyclerView;

/**
 * Description:通过YRecycleView实现列表添加头布局
 * AUTHOR: Champion Dragon
 * created at 2019/2/28
 **/
public class AddHeadViewActivity extends BaseActivity implements YRecyclerView.OnRefreshAndLoadMoreListener {

    private YRecyclerView rcv_empty_view;
    private ArrayList<String> arrayList = new ArrayList<>();
    private DemoAdapter demoAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_head_view);
        rcv_empty_view = (YRecyclerView) findViewById(R.id.rcv_empty_view);
        rcv_empty_view.setLayoutManager(new LinearLayoutManager(this));

        View headView = View.inflate(this, R.layout.head_custom, null);
        rcv_empty_view.addHeadView(headView);
        demoAdapter = new DemoAdapter(arrayList);
        rcv_empty_view.setAdapter(demoAdapter);
        rcv_empty_view.setRefreshAndLoadMoreListener(this);
        rcv_empty_view.showLoadingEmptyView("没数据,等着吧");
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                rcv_empty_view.setLoadingEmptyViewGone();
                demoAdapter.addReFreshData();
                rcv_empty_view.setReFreshComplete();
            }
        }, 2500);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                demoAdapter.addRLoadMOreData();
                rcv_empty_view.setReFreshComplete();
            }
        }, 2500);
    }
}

