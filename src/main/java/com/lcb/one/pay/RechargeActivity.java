package com.lcb.one.pay;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lcb.one.R;
import com.lcb.one.base.BaseActivity;
import com.lcb.one.base.BaseApplication;
import com.lcb.one.constant.Constant;
import com.lcb.one.http.HttpByGet;
import com.lcb.one.wxapi.WXPayEntryActivity;

/**
 * Description: 支付界面
 * AUTHOR: Champion Dragon
 * created at 2017/9/23
 **/
public class RechargeActivity extends BaseActivity implements View.OnClickListener {
    String tag="RechargeActivity";
    private EditText mMoneyEditText;
    private Button mPayButton;
    //    private TextView mHintTextView;
    private TextView mRechargeActivityTextView;
    private TextView mQuickInputTextView1;
    private TextView mQuickInputTextView2;
    private TextView mQuickInputTextView3;
    private TextView mQuickInputTextView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recharge);
        initview();
        onClick(mQuickInputTextView3);//默认选择第三个价位
        payTv();
    }

    /*向后台读取支付说明*/
    private void payTv() {
        String url = Constant.TcbUrl + "carinter.do";
        url = HttpByGet.getUrl(url, "p", "android", "v", "2410", "action", "getchargewords", "mobile", Constant.TcbMoible);
//        Logs.i(tag+"  45     " + url);
        mRechargeActivityTextView = (TextView) findViewById(R.id.tv_recharge_activity);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (!TextUtils.isEmpty(s)) {
                    mRechargeActivityTextView.setText(s);
                }
            }
        }, null);
        BaseApplication.getInstance().addToRequestQueue(request, this);
    }

    private void initview() {
        mMoneyEditText = (EditText) findViewById(R.id.et_recharge_money);
        mMoneyEditText.addTextChangedListener(mWatcher);
        mPayButton = (Button) findViewById(R.id.bt_recharge_pay);
        mPayButton.setOnClickListener(this);
        mQuickInputTextView1 = (TextView) findViewById(R.id.tv_recharge_1);
        mQuickInputTextView1.setOnClickListener(this);
        mQuickInputTextView2 = (TextView) findViewById(R.id.tv_recharge_2);
        mQuickInputTextView2.setOnClickListener(this);
        mQuickInputTextView3 = (TextView) findViewById(R.id.tv_recharge_3);
        mQuickInputTextView3.setOnClickListener(this);
        mQuickInputTextView4 = (TextView) findViewById(R.id.tv_recharge_4);
        mQuickInputTextView4.setOnClickListener(this);

    }

    /**
     * 设置输入金额的监听
     */
    TextWatcher mWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String str = s.toString();
            if (s.length() > 1 && str.startsWith("0")
                    && !str.startsWith("0.")) {
                mMoneyEditText.setText(str.substring(1));// 设置首字母不能输入0
                mMoneyEditText.setSelection(mMoneyEditText.getText()
                        .length());
                return;
            }
            if (str.contains(".")
                    && str.indexOf(".") < s.length() - 3) {
                mMoneyEditText.setText(str.substring(0,
                        str.indexOf(".") + 3));// 设置最低充值到：分
                mMoneyEditText.setSelection(mMoneyEditText.getText()
                        .length());//设置编辑器游标的位置
                return;
            }
            //当输入的金额不等于金额选项里的金额清空所有选项
            if (!str.equals(mQuickInputTextView1.getText())
                    && !str.equals(mQuickInputTextView2.getText())
                    && !str.equals(mQuickInputTextView3.getText())
                    && !str.equals(mQuickInputTextView4.getText())) {
                clearSelection();
            }
        }
    };


    /*清空所有选项*/
    private void clearSelection() {
        mQuickInputTextView1.setSelected(false);
        mQuickInputTextView2.setSelected(false);
        mQuickInputTextView3.setSelected(false);
        mQuickInputTextView4.setSelected(false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_recharge_pay:
                onPayButtonClicked();
                break;
            default:
                clearSelection();
                v.setSelected(true);
                mMoneyEditText.setText(((TextView) v).getText());
                mMoneyEditText.setSelection(mMoneyEditText.getText().length());
                break;
        }
    }


    /*跳转到支付方式选择的界面 */
    private void onPayButtonClicked() {
        String money = mMoneyEditText.getText().toString();//支付金额
        Intent intent = new Intent(this, WXPayEntryActivity.class);
        //设置参数
        intent.putExtra(WXPayEntryActivity.ARG_TOTALFEE, money);
        String subject = "账户充值";
        String prodType = WXPayEntryActivity.PROD_RECHARGE;

        intent.putExtra(WXPayEntryActivity.ARG_PRODTYPE,
                prodType);
        intent.putExtra(WXPayEntryActivity.ARG_SUBJECT, subject);

        startActivity(intent);


    }


}
