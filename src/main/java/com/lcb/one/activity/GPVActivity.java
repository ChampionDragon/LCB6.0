package com.lcb.one.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.lcb.one.R;
import com.lcb.one.base.BaseActivity;
import com.lcb.one.util.Logs;
import com.lcb.one.util.ToastUtil;
import com.lcb.one.view.GridPasswordView.GridPasswordView;
import com.lcb.one.view.GridPasswordView.PasswordType;


public class GPVActivity extends BaseActivity {
    String tag = "GPVActivity";
    private GridPasswordView ui, transformation, pwdtype, twiceInput;
    private Spinner gpvSpinner;
    private Switch gpvSwitch;

    private TextView tv;
    boolean isFirst = true;
    String firstPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpv);
        initView();
        onPwdChangedTest();
        /*解决输入时键盘遮挡问题*/
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }


    private void initView() {
        findViewById(R.id.back_gpv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ui = (GridPasswordView) findViewById(R.id.gpv_customUi);
        transformation = (GridPasswordView) findViewById(R.id.gpv_transformation);
        pwdtype = (GridPasswordView) findViewById(R.id.gpv_passwordType);
        twiceInput = (GridPasswordView) findViewById(R.id.gpv_normail_twice);
        gpvSpinner = (Spinner) findViewById(R.id.pswtype_sp);
        gpvSpinner.setOnItemSelectedListener(itemListener);
        gpvSwitch = (Switch) findViewById(R.id.psw_visibility_switcher);
        gpvSwitch.setOnCheckedChangeListener(checkListener);
         /*默认打开状态*/
        gpvSwitch.setChecked(true);

        tv = (TextView) findViewById(R.id.gpv_news);
    }

    /*设置是否显示密码*/
    CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Logs.v(tag + " 61 " + isChecked);
            ui.togglePasswordVisibility();
            transformation.togglePasswordVisibility();
            pwdtype.togglePasswordVisibility();
            twiceInput.togglePasswordVisibility();
        }
    };


    /*监听选择的方式*/
    AdapterView.OnItemSelectedListener itemListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    pwdtype.setPasswordType(PasswordType.NUMBER);
                    break;

                case 1:
                    pwdtype.setPasswordType(PasswordType.TEXT);
                    break;

                case 2:

                    pwdtype.setPasswordType(PasswordType.TEXTVISIBLE);
                    break;

                case 3:
                    pwdtype.setPasswordType(PasswordType.TEXTWEB);
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            ToastUtil.showLong("请选中其中一种密码方式");
        }
    };


//在OnPasswordChangedListener中测试GridPasswordView.clearPassword（）。需要输入密码两次，然后检查密码，如支付宝

    private void onPwdChangedTest() {
        twiceInput.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {
                if (psw.length() == 6 && isFirst) {
                    twiceInput.clearPassword();
                    isFirst = false;
                    firstPwd = psw;
                    tv.setText("请再输一次密码");
                } else if (psw.length() == 6 && !isFirst) {
                    if (psw.equals(firstPwd)) {
                        Log.d("lcb", tag + "  112  The password is: " + psw);
                        tv.setText("两次密码输入一样，为：" + psw);
                    } else {
                        Log.d("lcb", tag + "  114  password doesn't match the previous one, try again!");
                        tv.setText("密码两次输入不一致,请重试!");
                    }
                    twiceInput.clearPassword();
                    isFirst = true;
                }
            }

            @Override
            public void onInputFinish(String psw) {
                Logs.i(tag + " 124 " + psw);
            }
        });
    }
}
