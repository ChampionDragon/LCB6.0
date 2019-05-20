package com.lcb.one.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.lcb.one.R;
import com.lcb.one.RefreshRecyclerView.MultiTypeAdapter;
import com.lcb.one.RefreshRecyclerView.RefreshRecyclerView;
import com.lcb.one.adapter.CardRecordHolder;
import com.lcb.one.adapter.ImageViewHolder;
import com.lcb.one.adapter.TextImageViewHolder;
import com.lcb.one.adapter.TextViewHolder;
import com.lcb.one.bean.CardRecord;
import com.lcb.one.bean.TextImage;
import com.lcb.one.constant.Constant;
import com.lcb.one.util.TimeUtil;

/**
 * Description:复杂数据类型的RecyclerView
 * AUTHOR: Champion Dragon
 * created at 2019/5/7
 **/
public class MultiTypeActivity extends AppCompatActivity {

    private RefreshRecyclerView mRecyclerView;
    private MultiTypeAdapter mAdapter;
    private int mPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_type);
        mRecyclerView = (RefreshRecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MultiTypeAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addRefreshAction(() -> getData(true));
        mRecyclerView.addLoadMoreAction(() -> getData(false));
        mRecyclerView.addLoadMoreErrorAction(() -> getData(false));
        mRecyclerView.post(() -> {
            mRecyclerView.showSwipeRefresh();
            getData(true);
        });

        FloatingActionButton mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(v -> mAdapter.add(ImageViewHolder.class, getImageVirtualData()));
    }

    public void getData(final boolean isRefresh) {
        if (isRefresh) {
            mPage = 1;
        } else {
            mPage++;
        }
        if (mPage == 3) {
            mAdapter.showLoadMoreError();
            return;
        }
        mRecyclerView.postDelayed(() -> {
            if (isRefresh) {
                mAdapter.clear();
                mRecyclerView.dismissSwipeRefresh();
                mRecyclerView.getRecyclerView().scrollToPosition(0);
            }
            mAdapter.add(ImageViewHolder.class, getImageVirtualData());
            mAdapter.addAll(TextViewHolder.class, getTextVirtualData());
            mAdapter.addAll(TextImageViewHolder.class, getTextImageVirualData());
            mAdapter.addAll(CardRecordHolder.class, getRecordVirtualData());
            if (mPage >= 5) {
                mAdapter.showNoMore();
            }
        }, 1000);
    }

    public String getImageVirtualData() {
        return "http://i03.pictn.sogoucdn.com/3c28af542f2d49f7-fe9c78d2ff4ac332-73d7732e20e2fcfaa954979d623bcbe9_qq";
    }

    public String[] getTextVirtualData() {
        return new String[]{
                "计算机相关知识科普博客还有他",
                "技术职级规律越来越摸"
        };
    }

    public TextImage[] getTextImageVirualData() {
        return new TextImage[]{
                new TextImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1557044976994&di=39984bd3d5b9c3a64e61d41cde8e1754&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Ftranslate%2F199%2Fw600h399%2F20180628%2FyT2O-heqpwqy3038976.jpg", "数据1 " + TimeUtil.getSystem()),
                new TextImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1557044976995&di=8b14da11927bf351af7ccf847fbec561&imgtype=0&src=http%3A%2F%2Fimg1.dzwww.com%3A8080%2Ftupian%2F20160824%2F26%2F11200668837129743590.jpg", "数据2 " + TimeUtil.getSystem()),
                new TextImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1557044976995&di=5fe0969eaffb73f9701f6815221df53d&imgtype=0&src=http%3A%2F%2Fwww.thepaper.cn%2Fwww%2Fimage%2F4%2F41%2F72.jpg", "数据3 " + TimeUtil.getSystem())
        };
    }

    public CardRecord[] getRecordVirtualData() {
        return new CardRecord[]{
                new CardRecord("赣A12345", Constant.time, "南昌市红谷中大道", "驾驶人未按照规定使用安全带", 50, 2),
                new CardRecord("赣A12346", Constant.time1, "南昌市洪城路", "机动车违反禁止线指示", 200, 3),
        };
    }
}
