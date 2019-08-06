package com.lcb.one.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lcb.one.MainActivity;
import com.lcb.one.R;
import com.lcb.one.base.BaseActivity;
import com.lcb.one.constant.Constant;
import com.lcb.one.util.FileProvider7;
import com.lcb.one.util.Logs;
import com.lcb.one.util.SmallUtil;
import com.lcb.one.util.TimeUtil;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Description: 解决安卓7.0禁止在您的应用外部公开 file:// URI。如果一项包含文件 URI 的 intent 离开您的应用，则应用出现故障，
 * 并出现 FileUriExposedException 异常。要在应用间共享文件，您应发送一项 content:// URI，并授予 URI 临时访问权限。进行此授权的最简单方式是使用 FileProvider 类。
 * AUTHOR: Champion Dragon
 * created at 2019/8/5
 **/
public class FileProvider7Activity extends BaseActivity {
    private static final int REQUEST_CODE_TAKE_PHOTO = 0x110;
    private String mCurrentPhotoPath;
    private ImageView mIvPhoto;
    private TextView install;
    private int countdown;
    private File file;//拍照原图

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AskPermission();
        setContentView(R.layout.activity_file_provider7);
        mIvPhoto = findViewById(R.id.id_iv);
        install = findViewById(R.id.install);
        new TimeCount(20000, 1000).start();
    }

    private void AskPermission() {
        AndPermission.with(this).runtime().permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        /*同意权限集合*/
                        Logs.d(Arrays.toString(data.toArray()));
                    }
                })
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        /*禁止的权限集合*/
                        Logs.i(Arrays.toString(data.toArray()));
                    }
                }).start();

    }

    public void installApk(View view) {
        installApk();
    }

    /*需要自己修改安装包路径*/
    private void installApk() {
        File file = new File(Environment.getExternalStorageDirectory(),
                "app-debug.apk");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        FileProvider7.setIntentDataAndType(this,
                intent, "application/vnd.android.package-archive", file, true);
        startActivity(intent);
    }


    public void takePhotoNoCompress(View view) {
        takePhotoNoCompress();
    }


    private void takePhotoNoCompress() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            String filename = "拍照测试.png";
            file = new File(Environment.getExternalStorageDirectory(), filename);
            mCurrentPhotoPath = file.getAbsolutePath();
            Uri fileUri = FileProvider7.getUriForFile(this, file);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PHOTO);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_TAKE_PHOTO) {
            mIvPhoto.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));
            Luban(mCurrentPhotoPath);
            addWatermark(mCurrentPhotoPath);
        }
    }

    /*加水印*/
    private void addWatermark(String mCurrentPhotoPath) {
        Bitmap originalBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
        File addWatermarkFile = new File(Constant.fileLCB, "加水印后的照片.png");
        int textSize = 110;//设置字体大小
        /*水印信息包括VIN码、时间（精确到秒）、位置（经纬度坐标或检验区域）*/
        String text = "vin码" + "_检验区域_" + TimeUtil.long2time(System.currentTimeMillis(),Constant.formatsecond);
        //添加水印文字位置。
        Bitmap bitmapaddWatermark = SmallUtil.addTextWatermark(originalBitmap, text, textSize, Color.RED, 0,
                originalBitmap.getHeight() * 9 / 10, true);
        SmallUtil.save(bitmapaddWatermark, addWatermarkFile, Bitmap.CompressFormat.JPEG, true);

    }

    /*鲁班压缩*/
    private void Luban(String mCurrentPhotoPath) {
        Luban.with(this)
                .load(file) // 传入要压缩的图片列表
                .ignoreBy(100)  // 忽略不压缩图片的KB大小
                .setTargetDir(Environment.getExternalStorageDirectory().toString()) // 设置压缩后文件存储位置
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        Logs.i("鲁班压缩后的位置：" + file.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                        Logs.e(e.toString());
                    }
                }).launch();    //启动压缩
    }


    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            countdown = (int) (millisUntilFinished / 1000);
            install.setText("倒计时：" + countdown);
            if (countdown < 10) {
                install.setText("抓紧检验,还剩" + countdown);
                install.setBackgroundColor(getResources().getColor(R.color.red_deep));
                install.setTextColor(ContextCompat.getColor(FileProvider7Activity.this, R.color.colorAccent));
            }
        }

        @Override
        public void onFinish() {
            install.setText("时间已经到了禁止使用");
            install.setEnabled(false);
        }
    }


}
