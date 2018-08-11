package com.lcb.one.wxapi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.lcb.one.R;
import com.lcb.one.constant.Constant;
import com.lcb.one.pay.AccountInfo;
import com.lcb.one.pay.AliPayResult;
import com.lcb.one.pay.Keys;
import com.lcb.one.pay.Order;
import com.lcb.one.pay.PayResult;
import com.lcb.one.pay.PayResultFragment;
import com.lcb.one.pay.PollingProtocol;
import com.lcb.one.pay.Rsa;
import com.lcb.one.util.Logs;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class WXPayEntryActivity extends AppCompatActivity implements
        IWXAPIEventHandler, android.view.View.OnClickListener {
    /*支付结果*/
    public static final int MSG_WHAT_PAYRESULT = 0;
    public static final int MSG_WHAT_SHOW_DIALOG = 1;
    static String tag = "WXPayEntryActivity";
    // 通用属性
    public static final String ARG_PRODTYPE = "ptype";// 产品类型
    public static final String ARG_SUBJECT = "subject";// 产品名称
    public static final String ARG_TOTALFEE = "total";// 金额

    // 购买包月产品属性
    public static final String ARG_MONTYLYPAY_ID = "productid";// 月卡ID
    public static final String ARG_MONTYLYPAY_NUMBER = "number";// 购买时长
    public static final String ARG_MONTYLYPAY_START = "start";// 起始月份

    // 支付停车费属性
    public static final String ARG_ORDER_ID = "orderid";// 订单ID

    // 直付停车费属性
    public static final String ARG_ORDER_UID = "uid";// 收费员编号

    // 购买停车券属性
    public static final String ARG_TICKET_VALUE = "value";
    public static final String ARG_TICKET_NUMBER = "number";

    // 产品类型：0-->账户充值；1-->包月产品；2-->停车费结算；3-->直付;4-->付小费
    public static final String PROD_RECHARGE = "0";
    public static final String PROD_MONTHLY_PAY = "1";
    public static final String PROD_PARKING_FEE = "2";
    public static final String PROD_PAY_MONEY = "3";
    public static final String PROD_PAY_TIP = "4";
    public static final String PROD_BUY_TICKET = "5";

    // 支付方式：
    public static final int PAYTYPE_ALIPAY = 1;// 支付宝支付
    public static final int PAYTYPE_WXPAY = 2;// 微信支付

    private boolean mWXPayResultGetted = true;// 获取到微信支付结果的标志位
    private TextView mWXPayTextView;
    private TextView mAliPayTextView;
    private CheckBox mWXPayCheckBox;
    private CheckBox mAliPayCheckBox;
    private View mAliPayView;
    private View mWXPayView;

    private IWXAPI iWXApi;
    private Keys mKey;

    private HashMap<String, String> mOrder;// 订单

    private int mPayType;// 支付方式：默认余额支付

    private ProgressDialog mPayDialog;

    private boolean mStartedThreePartPay;
    private boolean mResumed;

    private AccountInfo mAccountInfo;// 用户账户信息

    public static PayResultHandler mPayResultHandler;
    public static final int REQUEST_CODE_CHOOSE_TICKET = 0;

    /**
     * 利用Handler轮询取消息
     */
    private PollingProtocol mPollingProtocol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        if (savedInstanceState != null) {
            setIntent((Intent) savedInstanceState.getParcelable("intent"));
        }
        initData();
        initView();

    }

    /*初始化布局*/
    private void initView() {
        TextView subjectTextView = (TextView) findViewById(R.id.tv_pay_subject);
        subjectTextView.setText(mOrder.get(ARG_SUBJECT));
        findViewById(R.id.back_pay).setOnClickListener(this);
        TextView totalTextView = (TextView) findViewById(R.id.tv_pay_total);
        totalTextView.setText("￥" + mOrder.get(ARG_TOTALFEE));
        findViewById(R.id.bt_recharge_pay).setOnClickListener(this);
        String prodType = mOrder.get(ARG_PRODTYPE);
        Logs.v("tag  126 产品类型 " + prodType);
        showAliPayView(true);
        showWXPayView(true);
        refreshPayView(PAYTYPE_ALIPAY);
    }

    // 是否显示支付宝支付布局
    private void showAliPayView(boolean show) {
        mAliPayView = findViewById(R.id.rl_pay_alipay);
        if (show) {
            mAliPayView.setVisibility(View.VISIBLE);
            mAliPayView.setOnClickListener(this);
            mAliPayTextView = (TextView) findViewById(R.id.tv_pay_alipay);
            mAliPayCheckBox = (CheckBox) findViewById(R.id.cb_pay_alipay);
        } else {
            mAliPayView.setVisibility(View.GONE);
        }
    }

    // 是否显示微信支付布局
    private void showWXPayView(boolean show) {
        // 初始化微信支付布局
        mWXPayView = findViewById(R.id.rl_pay_wxpay);
        iWXApi = WXAPIFactory.createWXAPI(this, Keys.WXPAY_APPID);
        if (!(iWXApi.isWXAppInstalled() && iWXApi.isWXAppSupportAPI())) {
            mWXPayView.setVisibility(View.GONE);
            return;
        }
        if (show) {
            mWXPayView.setVisibility(View.VISIBLE);
            mWXPayView.setOnClickListener(this);
            mWXPayTextView = (TextView) findViewById(R.id.tv_pay_wxpay);
            mWXPayCheckBox = (CheckBox) findViewById(R.id.cb_pay_wxpay);
        } else {
            mWXPayView.setVisibility(View.GONE);
        }
    }

    /*刷新选择支付时的界面*/
    private void refreshPayView(int expectedPayType) {
        BigDecimal totalPay = new BigDecimal(mOrder.get(ARG_TOTALFEE)).setScale(2, BigDecimal.ROUND_CEILING);// 总停车费金额
        BigDecimal balance = new BigDecimal(0);
        if (mAccountInfo != null && !TextUtils.isEmpty(mAccountInfo.balance)) {
            balance = new BigDecimal(mAccountInfo.balance);
        }
        setSelectedPayView(totalPay, balance, expectedPayType);
    }


    /**
     * 设置选择的支付方式
     *
     * @param totalPay        用户实际支付的金额：总金额减掉停车券面额（如果停车券可用的话）
     * @param balance         用户账户余额：如果余额不可用，则为0
     * @param expectedPayType 期望的支付方式：用户主动点选的（微信或者支付宝，余额则忽略）
     */
    private void setSelectedPayView(BigDecimal totalPay, BigDecimal balance,
                                    int expectedPayType) {

        // 先清除已选择的支付方式
        clearSelectedPayView();

        // 计算余额+停车券是否够支付停车费
        BigDecimal extraMoney = totalPay.subtract(balance).setScale(2);
//        if (extraMoney.compareTo(new BigDecimal(0)) == 1) {// 余额不足
        String extra = extraMoney.toPlainString();

        mWXPayView.setClickable(true);
        mAliPayView.setClickable(true);

        switch (expectedPayType) {
            case PAYTYPE_WXPAY:
                // 微信支付
                setPayTypeToWX(extra);
                break;
            case PAYTYPE_ALIPAY:
                // 支付宝
                setPayTypeToAli(extra);
                break;
        }
//        }
    }

    /*设置支付宝为支付类型*/
    private void setPayTypeToAli(String money) {
        mAliPayTextView.setText("￥" + money);
        mAliPayCheckBox.setChecked(true);
        mPayType = PAYTYPE_ALIPAY;
    }

    /*设置微信为支付类型*/
    private void setPayTypeToWX(String money) {
        if (mWXPayTextView != null) {
            mWXPayTextView.setText("￥" + money);
        }
        mWXPayCheckBox.setChecked(true);
        mPayType = PAYTYPE_WXPAY;
    }

    // 清除当前选中的支付方式
    private void clearSelectedPayView() {

        if (mWXPayTextView != null) {
            mWXPayTextView.setText("");
            mWXPayCheckBox.setChecked(false);
        }
        if (mAliPayTextView != null) {
            mAliPayTextView.setText("");
            mAliPayCheckBox.setChecked(false);
        }
        mWXPayView.setClickable(true);
        mAliPayView.setClickable(true);
    }


    /*从Intent中解析出订单参数*/
    private void initData() {
        mKey = new Keys();
        mPayResultHandler = new PayResultHandler(this);

        // 初始化支付参数
        mOrder = new HashMap<>();
        mOrder.put(ARG_SUBJECT, getIntent().getStringExtra(ARG_SUBJECT));
        mOrder.put(ARG_TOTALFEE, getIntent().getStringExtra(ARG_TOTALFEE));// 订单总金额
        mOrder.put("money", getIntent().getStringExtra(ARG_TOTALFEE));// 扣除停车券还需实际支付金额
        mOrder.put(ARG_PRODTYPE, getIntent().getStringExtra(ARG_PRODTYPE));
        mOrder.put("mobile", Constant.TcbMoible);


//        Log.v("lcb", tag + " 265 " + getIntent().getStringExtra(ARG_SUBJECT) + "\n  1   " + getIntent().getStringExtra(ARG_TOTALFEE)
//                + "\n  2   " + getIntent().getStringExtra(ARG_TOTALFEE) + "\n  3   " + getIntent().getStringExtra(ARG_PRODTYPE));

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_pay_alipay:
                onAliPayViewClicked();
                break;
            case R.id.rl_pay_wxpay:
                onWXPayViewClicked();
                break;
            case R.id.bt_recharge_pay:
                onPayButtonClicked();
                break;
            case R.id.back_pay:
                finish();
                break;
        }

    }


    /*+++++++++++++   微信的回调接口  ++++++++++++ */
    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {

    }


    /*  ======================  支付结果的handler  ======================= */
    public static class PayResultHandler extends Handler {

        // private PayResultFragment payResultDialog;
        private WXPayEntryActivity mActivity;
        private PayResultFragment payResultDialog;

        public PayResultHandler(WXPayEntryActivity activity) {
            this.mActivity = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_WHAT_SHOW_DIALOG:
                    if (payResultDialog != null) {
                        if (mActivity.isWXPayEntryActivityResumed()
                                && !payResultDialog.isAlreadyShown()) {
                            payResultDialog.show(
                                    mActivity.getSupportFragmentManager(),
                                    "PayResult");
                        } else {
                            Logs.v("310  activity isn't resumed or payResultDialog already shown");
                        }
                    } else {
                        Logs.v("313  payResultDialog is null");
                    }
                    break;

                default:
                    PayResult payResult = new PayResult();
                    try {
                        if (msg.obj instanceof PayResult) {
                            payResult = (PayResult) msg.obj;
                        } else if (msg.obj instanceof Order) {
                            Order order = (Order) msg.obj;
                            if (Order.STATE_PAYED.equals(order.getState())) {
                                // 支付成功
                                payResult.errmsg = order.getOrderid();
                                payResult.tips = order.getBonusid();
                                payResult.result = PayResult.PAY_RESULT_SUCCESS;
                            } else if (Order.STATE_PAY_FAILED.equals(order
                                    .getState())) {
                                // 支付失败
                                payResult.result = PayResult.PAY_RESULT_FAILED;
                            }
                        } else {
                            throw new IllegalArgumentException("支付结果数据错误");
                        }
                        showPayResultDialog(payResult,
                                mActivity.mOrder.get(ARG_PRODTYPE));
                    } catch (Exception e) {
                        payResult.result = PayResult.PAY_RESULT_FAILED;
                        payResult.errmsg = "数据解析错误~";
                        payResult.tips = "";
                        showPayResultDialog(payResult,
                                mActivity.mOrder.get(ARG_PRODTYPE));
                        e.printStackTrace();
                    }
                    break;
            }
        }

        /**
         * 弹出支付结果对话框
         *
         * @param payResult 支付结果
         * @param prodType  购买的产品类型，可取值：PROD_TYPE_MONTHLYPAY，PROD_TYPE_PARKINGFEE，
         *                  PROD_TYPE_RECHARGE，PROD_TYPE_PAYMONEY
         */
        public void showPayResultDialog(final PayResult payResult,
                                        final String prodType) {

            Log.d("lcb", tag + "   " + payResult + "  " + prodType);

            // 隐藏payDialog
            mActivity.dismissDialog();

            // 将WXPayEntryActivity中微信支付结果标志位置为true
            mActivity.mWXPayResultGetted = true;
            mActivity.mStartedThreePartPay = false;

            payResultDialog = PayResultFragment.newInstance(payResult, prodType);
            if (mActivity.isWXPayEntryActivityResumed()
                    && !payResultDialog.isAlreadyShown()) {
                payResultDialog.show(mActivity.getSupportFragmentManager(),
                        "PayResult");
            }
        }

    }

    /**
     * 取消payDialog的显示
     */
    public void dismissDialog() {
        if (mPayDialog != null && mPayDialog.isShowing()) {
            mPayDialog.dismiss();
        }
    }


    public boolean isWXPayEntryActivityResumed() {
        return mResumed;
    }

    public void setWXPayEntryActivityResumed(boolean mResumed) {
        this.mResumed = mResumed;
    }

    private void onWXPayViewClicked() {
        refreshPayView(PAYTYPE_WXPAY);
    }

    private void onAliPayViewClicked() {
        refreshPayView(PAYTYPE_ALIPAY);
    }

    /*+++++++++++++++++++++++++++++++++=   支付按钮   =+++++++++++++++++++++++++++++++++++++*/
    private void onPayButtonClicked() {
        if (!PROD_RECHARGE.equals(mOrder.get(ARG_PRODTYPE))
                && mAccountInfo == null) {
            Toast.makeText(this, "账户异常，请稍后重试~", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (mPayType) {
            case PAYTYPE_ALIPAY:
                aliPay();
                break;
            case PAYTYPE_WXPAY:
                wxPay();
                break;
        }
    }

    /*====================================   支付宝支付  =====================================*/
    public void aliPay() {

        onPrePay(PAYTYPE_ALIPAY);

        String info = getOrderInfo();

//        String sign = Rsa.sign(info, mKey.getPrivate());
        String sign = Rsa.sign(info, Constant.aliSign);

        try {
            sign = URLEncoder.encode(sign, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        info += "&sign=\"" + sign + "\"&" + getSignType();

        Logs.i(tag + "  442  aliPay params: --->> " + info);

        final String aliPayOrder = info;
        final PayTask aliPay = new PayTask(this);
        new Thread(new Runnable() {

            @Override
            public void run() {

                AliPayResult aliPayResult = new AliPayResult(
                        aliPay.pay(aliPayOrder));
                try {
                    Logs.i(tag + " 453  AliPay Result: --->> " + aliPayResult.getResult());
                    aliPayResult.parseResult();
                    switch (aliPayResult.resultStatus) {
                        case AliPayResult.ERR_SUCCESS:// 支付宝支付成功，不错处理，等待服务器通知
                            if (aliPayResult.isSignOk) {// 判断签名是否正确
                                Logs.i(tag + "   AliPay Result: --->> Success!");
                                mStartedThreePartPay = true;
                            }
                            return;
                    }
                    Message.obtain(
                            mPayResultHandler,
                            MSG_WHAT_PAYRESULT,
                            new PayResult(PayResult.PAY_RESULT_FAILED,
                                    aliPayResult.resultStatus,
                                    aliPayResult.tips)).sendToTarget();
                } catch (Exception e) {
                    Message.obtain(
                            mPayResultHandler,
                            MSG_WHAT_PAYRESULT,
                            new PayResult(PayResult.PAY_RESULT_FAILED,
                                    aliPayResult.resultStatus,
                                    aliPayResult.tips)).sendToTarget();
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 支付前的预处理：显示"支付中..."Dialog，开启线程从服务器获取支付结果
     */
    private void onPrePay(int payType) {
        if (mPayDialog != null && mPayDialog.isShowing()) {
            return;
        }

        this.mPayType = payType;

        String paying = "支付中...";
        switch (payType) {
            case PAYTYPE_ALIPAY:
                paying = "支付宝支付中...";
                break;
            case PAYTYPE_WXPAY:
                paying = "微信支付中...";
                mWXPayResultGetted = false;
                break;
        }

//        mPayDialog = ProgressDialog.show(this, "", paying, false, true);
//        mPayDialog.setCanceledOnTouchOutside(false);

    }


    // 拼装支付宝请求的请求参数
    private String getOrderInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("partner=\"");
//        sb.append(mKey.getPartner());
        sb.append("2088411488582814");//支付宝的身份ID，需自行申请
        sb.append("\"&out_trade_no=\"");
        sb.append(getOutTradeNo());
        sb.append("\"&subject=\"");
        sb.append(mOrder.get(ARG_SUBJECT));
        sb.append("\"&body=\"");
        // 支付宝支付分两步：1账户充值，2余额支付
        sb.append(getBody());
        sb.append("\"&total_fee=\"");
        sb.append(mAliPayTextView.getText().toString().replace("￥", ""));
        sb.append("\"&service=\"mobile.securitypay.pay");
        sb.append("\"&_input_charset=\"UTF-8");

        // TODO 新版支付宝貌似不再需要此参数
        sb.append("\"&return_url=\"");
        try {
            sb.append(URLEncoder.encode("http://m.alipay.com", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        sb.append("\"&payment_type=\"1");
        sb.append("\"&seller_id=\"");
        sb.append("caiwu@zhenlaidian.com");
//        sb.append(mKey.getSeller());
        sb.append("\"&notify_url=\"");
//        sb.append(mKey.getAliPayNotifyUrl());
        sb.append("caiwu@zhenlaidian.com");
        sb.append("\"");
        return new String(sb);
    }


    // 支付宝：生成交易的唯一订单号
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
                Locale.CHINA);
        Date date = new Date();
        String key = format.format(date);

        java.util.Random r = new java.util.Random();
        key += r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    // 支付宝：获取签名方法
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }


    /*  ++++++++++++++++++++++++++++++++++   微信支付  ++++++++++++++++++++++++++++++++  */
    public void wxPay() {
        // 微信支付
        // http://192.168.199.240/zld/wxpreorder.do?action=preorder&body=producttest&total_fee=100&attach="";
        iWXApi.registerApp(Keys.WXPAY_APPID);

        Map<String, String> wxOrder = new HashMap<>();
        try {
            wxOrder.put("body",
                    URLEncoder.encode(mOrder.get(ARG_SUBJECT), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        wxOrder.put("total_fee",
                mWXPayTextView.getText().toString().replace("￥", ""));
        wxOrder.put("action", "preorder");
        // 微信支付分两步：先给账户充值，再判断需不需要支付停车费或者购买包月产品
        wxOrder.put("attach", getBody());
        String url = Constant.TcbUrl + "wxpreorder.do";
        Logs.i(tag + " 589 getPrepayOrder url: --->> " + url + "\nparams: --->> " + wxOrder.toString());

        onPrePay(PAYTYPE_WXPAY);

        new AQuery(this).ajax(url, wxOrder, String.class,
                new AjaxCallback<String>() {
                    @Override
                    public void callback(String url, String object,
                                         AjaxStatus status) {
                        Logs.d(tag + " 597 wxPay result: --->>" + object);
                        String errMsg = "网络超时";
                        if (!TextUtils.isEmpty(object)) {
                            try {
                                PayReq req = new Gson().fromJson(object,
                                        PayReq.class);
                                if (!TextUtils.isEmpty(req.prepayId)) {
                                    req.appId = Keys.WXPAY_APPID;
                                    req.partnerId = Keys.WXPAY_PARTNERID;
                                    req.packageValue = "Sign=WXpay";
                                    iWXApi.sendReq(req);
                                    return;
                                } else {
                                    errMsg = "获取预支付订单异常";
                                }
                            } catch (Exception e) {
                                errMsg = "服务器错误";
                                e.printStackTrace();
                            }
                        }
                        Message.obtain(
                                mPayResultHandler,
                                MSG_WHAT_PAYRESULT,
                                new PayResult(PayResult.PAY_RESULT_FAILED,
                                        errMsg, AliPayResult.TIP_PAY_ERROR))
                                .sendToTarget();
                        super.callback(url, object, status);
                    }
                });
    }


    // 支付宝&微信： 拼装支付宝请求参数中的"body"参数&微信支付中的"attach"参数
    private String getBody() {
        String extra = "";
        switch (mOrder.get(ARG_PRODTYPE)) {
            case PROD_RECHARGE:// 账户充值
                break;
            case PROD_MONTHLY_PAY:// 购买包月产品
                extra = "_" + mOrder.get(ARG_MONTYLYPAY_ID) + "_"
                        + mOrder.get(ARG_MONTYLYPAY_NUMBER) + "_"
                        + mOrder.get(ARG_MONTYLYPAY_START);
                break;
            case PROD_PARKING_FEE:// 停车费结算
                extra = "_" + mOrder.get(ARG_ORDER_ID) + "_"
                        + mOrder.get("ticketid");
                break;
            case PROD_PAY_MONEY:// 直接支付停车费
                extra = "_" + mOrder.get(ARG_ORDER_UID) + "_"
                        + mOrder.get(ARG_TOTALFEE) + "_" + mOrder.get("ticketid");
                break;
            case PROD_PAY_TIP:// 付小费
                extra = "_" + mOrder.get(ARG_ORDER_UID) + "_"
                        + mOrder.get(ARG_TOTALFEE) + "_"
                        + mOrder.get(ARG_ORDER_ID) + "_" + mOrder.get("ticketid");
                break;
            case PROD_BUY_TICKET:
                extra = "_" + mOrder.get(ARG_TICKET_VALUE) + "_" + mOrder.get(ARG_TICKET_NUMBER);
                break;
        }
        return mOrder.get("mobile") + "_" + mOrder.get(ARG_PRODTYPE) + extra;
    }


}
