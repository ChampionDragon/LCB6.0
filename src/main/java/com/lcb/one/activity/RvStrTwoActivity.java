package com.lcb.one.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lcb.one.R;
import com.lcb.one.base.BaseActivity;
import com.lcb.one.rvstrtwo.DataBean;
import com.lcb.one.rvstrtwo.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: RecyclerView二级Strings列表
 * AUTHOR: Champion Dragon
 * created at 2018/4/25
 **/
public class RvStrTwoActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private List<DataBean> dataBeanList;
    private DataBean dataBean;
    private RecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv_str_two);
        initView();
        initData();
    }

    /*模拟数据*/
    private void initData() {
        List<String> strs = new ArrayList<>();
        strs.add("10:55红外 开启");
        strs.add("12:35设备故障");
        strs.add("23:55用户XXXXXXXXXXX下线");
        strs.add("07:21用户YYYYYYYYYYY上线");
        strs.add("16:46红外 关闭");
        dataBeanList = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            dataBean = new DataBean();
            dataBean.setID(i + "");
            dataBean.setListdata(strs);
            dataBean.setType(0);
            dataBean.setParentLeftTxt("父--" + i);
            dataBean.setChildLeftTxt("子--" + i);
            dataBean.setChildRightTxt("子内容--" + i);
            dataBean.setChildBean(dataBean);
            dataBeanList.add(dataBean);
        }
        setData();
    }


    private void setData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecyclerAdapter(this, dataBeanList);
        mRecyclerView.setAdapter(mAdapter);
        //滚动监听
        mAdapter.setOnScrollListener(new RecyclerAdapter.OnScrollListener() {
            @Override
            public void scrollTo(int pos) {
                mRecyclerView.scrollToPosition(pos);
            }
        });
    }

    private void initView() {
        findViewById(R.id.rvstrtwo).setOnClickListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rvstrtwo:
                finish();
                break;
        }
    }
}
