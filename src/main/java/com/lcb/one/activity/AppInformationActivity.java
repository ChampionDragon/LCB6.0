package com.lcb.one.activity;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.lcb.one.R;
import com.lcb.one.adapter.InfoAdapter;
import com.lcb.one.base.BaseActivity;
import com.lcb.one.bean.InfoBean;
import com.lcb.one.listener.PermissionListener;
import com.lcb.one.util.SystemUtil;

import java.util.List;

public class AppInformationActivity extends BaseActivity {
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_information);
        findViewById(R.id.back_appinfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        askForPermission();
    }

    /*动态申请权限:访问电话状态*/
    private void askForPermission() {
        requestRunPermisssion(new String[]{Manifest.permission.READ_PHONE_STATE}, new PermissionListener() {
            @Override
            public void onGranted() {
                initData();
            }

            @Override
            public void onDenied(List<String> deniedPermission) {
                String s = "被拒绝的权限：";
                for (String permission : deniedPermission) {
                    s += permission + "\n";
                }
                Toast.makeText(AppInformationActivity.this, s, Toast.LENGTH_SHORT).show();
                //提示用户到系统去设置权限
                msg_one();
            }
        });
    }

    /**
     * 初始化列表数据
     */
    private void initData() {
        InfoAdapter adapter = new InfoAdapter(this, R.layout.item_info);
        lv = (ListView) findViewById(R.id.appinfo_lv);
        adapter.add(new InfoBean("app的签名", SystemUtil.AppSignature()));
        adapter.add(new InfoBean("app的名称", SystemUtil.AppName()));
        adapter.add(new InfoBean("app的版本号", SystemUtil.VersionCode() + ""));
        adapter.add(new InfoBean("app的版本号名", SystemUtil.VersionName()));
        adapter.add(new InfoBean("app的包名", SystemUtil.PackgeName()));
        adapter.add(new InfoBean("手机的IMEI号", SystemUtil.IMEI()));
        adapter.add(new InfoBean("手机的IMSI", SystemUtil.IMSI()));
        adapter.add(new InfoBean("手机的号码", SystemUtil.Num()));
        adapter.add(new InfoBean("手机产品的序列号", SystemUtil.SN()));
        adapter.add(new InfoBean("手机的sim号", SystemUtil.SIM()));
        adapter.add(new InfoBean("手机的ID", SystemUtil.ID()));
        adapter.add(new InfoBean("手机的mac地址", SystemUtil.MAC()));
        adapter.add(new InfoBean("系统国家", SystemUtil.Country()));
        adapter.add(new InfoBean("系统语言", SystemUtil.Language()));
        adapter.add(new InfoBean("屏幕的高", SystemUtil.Height() + ""));
        adapter.add(new InfoBean("屏幕的宽", SystemUtil.Width() + ""));
        adapter.add(new InfoBean("屏幕的密度", SystemUtil.densityDpi() + ""));
        adapter.add(new InfoBean("屏幕的密度比", SystemUtil.Density() + ""));
        adapter.add(new InfoBean("系统版本名", Build.VERSION.RELEASE));
        adapter.add(new InfoBean("系统版本号", Build.VERSION.SDK_INT + ""));
        adapter.add(new InfoBean("系统型号", Build.MODEL));
        adapter.add(new InfoBean("系统定制商", Build.BRAND));
        adapter.add(new InfoBean("系统的主板", Build.BOARD));
        adapter.add(new InfoBean("手机制造商", Build.PRODUCT));
        adapter.add(new InfoBean("系统2", Build.HOST));
        adapter.add(new InfoBean("系统3", Build.TIME + "    " + System.currentTimeMillis()));
        adapter.add(new InfoBean("系统4", Build.USER));
        adapter.add(new InfoBean("系统硬件执照商", Build.MANUFACTURER));
        adapter.add(new InfoBean("builder类型", Build.MANUFACTURER));
        lv.setAdapter(adapter);
    }


}
