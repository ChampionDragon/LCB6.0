package com.lcb.one.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lcb.one.R;
import com.lcb.one.RefreshRecyclerView.Action;
import com.lcb.one.RefreshRecyclerView.RefreshRecyclerView;
import com.lcb.one.adapter.CardRecordAdapter;
import com.lcb.one.bean.CardRecord;
import com.lcb.one.bean.Rvdata;
import com.lcb.one.constant.Constant;
import com.lcb.one.listener.RvListener;
import com.lcb.one.util.DpUtil;
import com.lcb.one.util.Logs;

/**
 * Description:加载刷新小卡片模式的RecyclerView
 * AUTHOR: Champion Dragon
 * created at 2019/5/6
 **/
public class RvCard extends AppCompatActivity {
    private RefreshRecyclerView mRecyclerView;
    private CardRecordAdapter mAdapter;
    private Handler mHandler;
    private int page = 1;
    private RvListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv_card);
        mHandler = new Handler();
        FloatingActionButton mFab = (FloatingActionButton) findViewById(R.id.fab);

        listener = new RvListener() {
            @Override
            public void setdata(Rvdata data) {
                Logs.w(data.getPosition() + " 位置");
                mAdapter.remove(data.getPosition());
            }
        };

        mAdapter = new CardRecordAdapter(this, listener);

        //添加Header
        final TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DpUtil.dip2px(this, 48)));
        textView.setTextSize(16);
        textView.setGravity(Gravity.CENTER);
        textView.setText("龙成斌的上拉刷新下拉加载");
        mAdapter.setHeader(textView);
        //添加footer
        final TextView footer = new TextView(this);
        footer.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DpUtil.dip2px(this, 48)));
        footer.setTextSize(16);
        footer.setGravity(Gravity.CENTER);
        footer.setText("-- Footer --");
        mAdapter.setFooter(footer);

        mRecyclerView = (RefreshRecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setAdapter(mAdapter);
        /*刷新按钮*/
        mRecyclerView.addRefreshAction(new Action() {
            @Override
            public void onAction() {
                getData(true);
            }
        });

        /*加载更多按钮*/
        mRecyclerView.addLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                getData(false);
                page++;
            }
        });

        /*点击加载错误按钮继续加载*/
        mRecyclerView.addLoadMoreErrorAction(new Action() {
            @Override
            public void onAction() {
                getData(false);
                page++;
            }
        });


        //默认打开界面加载数据
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.showSwipeRefresh();
                getData(true);
            }
        });
        //点击删除按钮默认删除第一个
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.remove(1);
            }
        });


    }

    public void getData(final boolean isRefresh) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isRefresh) {
                    page = 1;
                    mAdapter.clear();
                    mAdapter.addAll(getVirtualData());
                    mRecyclerView.dismissSwipeRefresh();
                    mRecyclerView.getRecyclerView().scrollToPosition(0);
                } else if (page == 4) {
                    mAdapter.showLoadMoreError();
                } else {
                    mAdapter.addAll(getVirtualData());
                    if (page >= 8) {
                        mRecyclerView.showNoMore();
                    }
                }
            }
        }, 1111);
    }

    private CardRecord[] getVirtualData() {
        return new CardRecord[]{
                new CardRecord("赣A12347", Constant.time2, "南昌市解放西路", "机动车行驶超过规定时速20%以上未达50%", 200, 6),
                new CardRecord("赣A12348", Constant.time3, "南昌市明德路", "机动车通过有灯控路口，不按所需行进方向驶入导向车道", 100, 2),
                new CardRecord("赣A12349", Constant.time4, "南昌市坛子口", "机动车行驶超过规定时速达50%以上", 200, 12),
                new CardRecord("赣AFW123", Constant.time5, "南昌市十字街", "机动车逆向行驶", 200, 3),
        };
    }


}
