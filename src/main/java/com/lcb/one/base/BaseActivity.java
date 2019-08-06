package com.lcb.one.base;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.lcb.one.listener.PermissionListener;
import com.lcb.one.util.SmallUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建程序的基本类
 */
public class BaseActivity extends AppCompatActivity {
    protected ProgressDialog mProgressDialog;
    public AsyncTaskExecutor executor;
    private PermissionListener mListener;//已授权、未授权的接口回调
    private static final int PERMISSION_REQUESTCODE = 11;//动态申请权限的码

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*设置成透明导航栏和透明状态栏*/
        SmallUtil.setScreen(this);
        executor = AsyncTaskExecutor.getinstance();
    }

    public void stopProgressDialog() {
        if (mProgressDialog.isShowing() && mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }


    /*  --------------------------- 动态申请权限 -----------------------------  */
    public void requestRunPermisssion(String[] permissions, PermissionListener listener) {
        mListener = listener;
        List<String> permissionLists = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionLists.add(permission);
            }
        }

        if (!permissionLists.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionLists.toArray(new String[permissionLists.size()]), PERMISSION_REQUESTCODE);
        } else {
            //表示全都授权了
            mListener.onGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUESTCODE:
                if (grantResults.length > 0) {
                    //存放没授权的权限
                    List<String> deniedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        String permission = permissions[i];
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            deniedPermissions.add(permission);
                        }
                    }
                    if (deniedPermissions.isEmpty()) {
                        //说明都授权了
                        mListener.onGranted();
                    } else {
                        mListener.onDenied(deniedPermissions);
                    }
                }
                break;
            default:
                break;
        }
    }


    /*提醒用户不开通权限的后果*/
    public void msg_one() {
        //当拒绝了授权后，为提升用户体验，可以以弹窗的方式引导用户到设置中去进行设置
        new AlertDialog.Builder(this)
                .setTitle("警告")
                .setMessage("需要开启权限才能使用此功能")
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //引导用户到设置中去进行设置
                        Intent intent = new Intent();
                        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.setData(Uri.fromParts("package", getPackageName(), null));
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", null)
                .create()
                .show();
    }


}
