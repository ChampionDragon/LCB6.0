package com.lcb.one.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewGroup;

import com.lcb.one.R;
import com.lcb.one.RefreshRecyclerView.BaseViewHolder;
import com.lcb.one.RefreshRecyclerView.CustomMultiTypeAdapter;
import com.lcb.one.RefreshRecyclerView.IViewHolderFactory;
import com.lcb.one.RefreshRecyclerView.RefreshRecyclerView;
import com.lcb.one.adapter.CardRecordHolder;
import com.lcb.one.adapter.ImageViewHolder;
import com.lcb.one.adapter.TextImageViewHolder;
import com.lcb.one.adapter.TextViewHolder;
import com.lcb.one.bean.CardRecord;
import com.lcb.one.bean.TextImage;
import com.lcb.one.constant.Constant;

/**
 * Description:复杂数据类型的RecyclerView(避免反射弊端)
 * 需要实现 IViewHolderFactory 接口类来管理viewtype 和 ViewHolder 的映射关系。
 * AUTHOR: Champion Dragon
 * created at 2019/5/7 0007
 **/
public class CustomMultiTypeActivity extends AppCompatActivity implements IViewHolderFactory {

    private RefreshRecyclerView mRecyclerView;
    private CustomMultiTypeAdapter mAdapter;
    private int mPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_multi_type);

        mRecyclerView = (RefreshRecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setSwipeRefreshColors(0xFF437845,0xFFE44F98,0xFF2FAC21);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CustomMultiTypeAdapter(this);
        mAdapter.setViewHolderFactory(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addRefreshAction(() -> getData(true));
        mRecyclerView.addLoadMoreAction(() -> getData(false));
        mRecyclerView.post(() -> {
            mRecyclerView.showSwipeRefresh();
            getData(true);
        });
        mRecyclerView.addLoadMoreErrorAction(() -> getData(false));

        FloatingActionButton mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(v -> mAdapter.add(getImageVirtualData(), VIEW_TYPE_IAMGE));
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
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isRefresh) {
                    mAdapter.clear();
                    mRecyclerView.dismissSwipeRefresh();
                }
                mAdapter.add(getImageVirtualData(), VIEW_TYPE_IAMGE);
                mAdapter.addAll(getTextVirtualData(), VIEW_TYPE_TEXT);
                mAdapter.addAll(getTextImageVirualData(), VIEW_TYPE_TEXT_IMAGE);
                mAdapter.addAll(getRecordVirtualData(), VIEW_TYPE_CARD);
                if (mPage >= 5) {
                    mAdapter.showNoMore();
                }
                if(isRefresh){
                    mRecyclerView.getRecyclerView().scrollToPosition(0);
                }
            }
        }, 1000);
    }

    public String getImageVirtualData() {
        return "http://i03.pictn.sogoucdn.com/3c28af542f2d49f7-fe9c78d2ff4ac332-73d7732e20e2fcfaa954979d623bcbe9_qq";
    }

    public String[] getTextVirtualData() {
        return new String[]{
                "算机相关知识科普博客还有他",
                "技术职级规律越来越摸"
        };
    }

    public TextImage[] getTextImageVirualData() {
        return new TextImage[]{
                new TextImage(Constant.img1, "风景图"),
                new TextImage(Constant.img2, "风景图"),
                new TextImage(Constant.img3, "风景图"),
        };
    }

    public CardRecord[] getRecordVirtualData() {
        return new CardRecord[]{
                new CardRecord("赣A12345", Constant.time, "南昌市红谷中大道", "驾驶人未按照规定使用安全带", 50, 2),
                new CardRecord("赣A12346", Constant.time1, "南昌市洪城路", "机动车违反禁止线指示", 200, 3),
        };
    }

    private final int VIEW_TYPE_TEXT = 128 << 1;
    private final int VIEW_TYPE_IAMGE = 128 << 2;
    private final int VIEW_TYPE_TEXT_IMAGE = 128 << 3;
    private final int VIEW_TYPE_CARD = 128<<4;

    @Override
    public <V extends BaseViewHolder> V getViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_TEXT:
                return (V) new TextViewHolder(parent);
            case VIEW_TYPE_IAMGE:
                return (V) new ImageViewHolder(parent);
            case VIEW_TYPE_TEXT_IMAGE:
                return (V) new TextImageViewHolder(parent);
            case VIEW_TYPE_CARD:
                return (V) new CardRecordHolder(parent);
            default:
                return (V) new TextViewHolder(parent);
        }
    }
}
