package com.lcb.one.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.lcb.one.R;
import com.lcb.one.base.BaseActivity;
import com.lcb.one.util.Logs;
import com.lcb.one.util.TimeUtil;
import com.lcb.one.view.TableView;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TableViewActivity extends BaseActivity {
    private TableView tableView;
    private String[] city = {"南昌", "深圳", "北京", "上海", "广州"};
    private String[] carNum = {"赣A12345", "赣A12435", "赣A1k345", "赣A123q5", "赣A1234p"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_view);
        tableView = (TableView) findViewById(R.id.table);
        initTableView();
        /*设置定时器更新数据*/
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        }, 5, 8, TimeUnit.SECONDS);
    }


    private void initTableView() {
        tableView.clearTableContents()
                .setHeader("区域", "人数", "占比")
                .addContent(city[newRandomNumber()], "30", "30%", carNum[newRandomNumber()], "测试")
                .addContent(city[newRandomNumber()], "30", "30%", carNum[newRandomNumber()], "测试")
                .addContent(city[newRandomNumber()], "20", "20%", carNum[newRandomNumber()], "测试")
                .addContent(city[newRandomNumber()], "10", "10%", carNum[newRandomNumber()], "测试")
                .addContent(city[newRandomNumber()], "10", "10%", carNum[newRandomNumber()], "测试")
                .setColumnWeights(2, 1, 1, 2, 1)//设置比重
                .refreshTable();
        Logs.d(TimeUtil.getSystem());
    }

    /*设置随机数*/
    private int newRandomNumber() {
        return (new Random().nextInt(5));
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    initTableView();
                    break;
            }
        }
    };


}
