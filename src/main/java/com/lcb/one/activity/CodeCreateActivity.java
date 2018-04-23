package com.lcb.one.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lcb.one.R;
import com.lcb.one.base.BaseActivity;
import com.lcb.one.zxing.main.CreateCodeUtil;
import com.lcb.one.zxing.main.MipcaActivityCapture;


/**
 * Description: 生成和扫描二维码
 * AUTHOR: Champion Dragon
 * created at 2017/10/20
 **/

public class CodeCreateActivity extends BaseActivity implements View.OnClickListener {
    private TextView result;
    private CheckBox isLogo;
    private ImageView imageView;
    private EditText et;
    private Button scanf, create;
    private final static int SCANNIN_GREQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_create);
        initView();
    }

    private void initView() {
        result = (TextView) findViewById(R.id.codecreate_result);
        isLogo = (CheckBox) findViewById(R.id.codecreate_logo);
        et = (EditText) findViewById(R.id.codecreate_string);
        imageView = (ImageView) findViewById(R.id.codecreate_image);
        findViewById(R.id.codecreate_create).setOnClickListener(this);
        findViewById(R.id.back_codecreate).setOnClickListener(this);
        findViewById(R.id.codecreate_scan).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.codecreate_create:
                create();
                break;
            case R.id.back_codecreate:
                finish();
                break;
            case R.id.codecreate_scan:
                scan();
                break;
        }
    }

    /*通过文字创建二维码*/
    private void create() {
        String contentString = et.getText().toString();
        if (!contentString.equals("")) {
            //根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（550*550）
            Bitmap qrCodeBitmap = CreateCodeUtil.createQRCode(contentString, 550, 550,
                    isLogo.isChecked() ?
                            BitmapFactory.decodeResource(getResources(), R.mipmap.bs_login) :
                            null);
            imageView.setImageBitmap(qrCodeBitmap);
        } else {
            Toast.makeText(CodeCreateActivity.this, "生成二维码出现错误请检查", Toast.LENGTH_SHORT).show();
        }

    }

    /*扫描功能*/
    private void scan() {
        Intent intent = new Intent();
        intent.setClass(CodeCreateActivity.this,
                MipcaActivityCapture.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    // 扫描到的内容
                    String str = bundle.getString("result");
                    result.setText(str);
                }
                break;
        }
    }
}

