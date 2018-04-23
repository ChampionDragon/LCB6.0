package com.lcb.one.pay.unionpay;

import android.app.Activity;
import android.widget.TextView;

import com.unionpay.UPPayAssistEx;

/**
 * Description: 银联支付启动类
 * AUTHOR: Champion Dragon
 * created at 2017/9/21
 **/
public class JARActivity extends UnionPayActivity {
    @Override
    public void doStartUnionPayPlugin(Activity activity, String tn, String mode) {
        UPPayAssistEx.startPay(activity, null, null, tn, mode);
    }

    @Override
    public void updateTextView(TextView tv) {
        String txt = "接入指南：\n1:拷贝sdkPro目录下的UPPayAssistEx.jar到libs目录下\n"
                + "2:根据需要拷贝sdkPro/jar/data.bin至工程的assets目录下\n"
                + "3:根据需要拷贝sdkPro/jar/XXX/XXX.so到libs目录下\n"
                + "4:根据需要拷贝sdkPro/jar/UPPayPluginExPro.jar到工程的libs目录下\n"
                + "5:获取tn后通过UPPayAssistEx.startPay(...)方法调用控件";
        tv.setText(txt);
    }
}
