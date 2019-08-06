package com.lcb.one.activity;

import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.lcb.one.R;
import com.lcb.one.base.BaseActivity;
import com.lcb.one.view.GridPasswordView.GridPasswordView;
import com.lcb.one.view.XKeyboardView;

/**
 * Description: 自定义车牌输入
 * AUTHOR: Champion Dragon
 * created at 2018/8/11
 **/
public class PlateNumberInputActivity extends BaseActivity {
    private GridPasswordView gpvPlateNumber;
    private XKeyboardView viewKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plate_number_input);
        initView();
        testPlateNumberInput();
    }

    /*初始化键盘和车牌号码输入框*/
    private void testPlateNumberInput() {
        viewKeyboard.setIOnKeyboardListener(new XKeyboardView.IOnKeyboardListener() {
            @Override
            public void onInsertKeyEvent(String text) {
                gpvPlateNumber.appendPassword(text);
            }

            @Override
            public void onDeleteKeyEvent() {
                gpvPlateNumber.deletePassword();
            }

            @Override
            public void hideKeyboard() {
                     /*隐藏键盘*/
                if (viewKeyboard.isShown()) {
                    viewKeyboard.setVisibility(View.GONE);
                }
            }
        });
        gpvPlateNumber.togglePasswordVisibility();
        gpvPlateNumber.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public boolean beforeInput(int position) {
                Log.v("lcb", position + "    ----");
                if (position == 0) {
                    viewKeyboard.setKeyboard(new Keyboard(PlateNumberInputActivity.this, R.xml.provice));
                    viewKeyboard.setVisibility(View.VISIBLE);
                    return true;
                } else if (position >= 1 && position < 2) {
                    viewKeyboard.setKeyboard(new Keyboard(PlateNumberInputActivity.this, R.xml.english));
                    viewKeyboard.setVisibility(View.VISIBLE);
                    return true;
                } else if (position >= 2 && position < 6) {
                    viewKeyboard.setKeyboard(new Keyboard(PlateNumberInputActivity.this, R.xml.qwerty_without_chinese));
                    viewKeyboard.setVisibility(View.VISIBLE);
                    return true;
                } else if (position >= 6 && position < 7) {
                    if (gpvPlateNumber.getPassWord().startsWith("粤Z")) {
                        viewKeyboard.setKeyboard(new Keyboard(PlateNumberInputActivity.this, R.xml.qwerty));
                    } else {
                        viewKeyboard.setKeyboard(new Keyboard(PlateNumberInputActivity.this, R.xml.qwerty_without_chinese));
                    }
                    viewKeyboard.setVisibility(View.VISIBLE);
                    return true;
                }
                viewKeyboard.setVisibility(View.GONE);
                return false;
            }

            @Override
            public void onTextChanged(String psw) {
                Log.e("lcb", "onTextChanged：" + psw);
            }

            @Override
            public void onInputFinish(String psw) {
                Log.e("lcb", "onInputFinish：" + psw);
            }
        });
    }

    private void initView() {
        findViewById(R.id.back_platenuminput).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        gpvPlateNumber = (GridPasswordView) findViewById(R.id.platenuminput_gpv);
        viewKeyboard = (XKeyboardView) findViewById(R.id.platenuminput_kb);
    }

    /*复写系统返回按钮，用户按返回键能隐藏自定义输入框*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (viewKeyboard.isShown()) {
                viewKeyboard.setVisibility(View.GONE);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
