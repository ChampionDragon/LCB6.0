package com.lcb.one.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lcb.one.R;
import com.lcb.one.base.BaseActivity;
import com.lcb.one.constant.Constant;
import com.lcb.one.util.Logs;
import com.lcb.one.util.NetConnectUtil;
import com.lcb.one.util.ToastUtil;
import com.lcb.one.wxapi.WXEntryActivity;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

public class Thirdlogin extends BaseActivity implements View.OnClickListener {
    private TextView tv;
    private Tencent mTencent;
    private String tag = "Thirdlogin";
    private String QQ_uid;//qq_openid
    private String wx_access_token, wx_openid;//微信登录返回得到openid，access_token;


    /*创建用于接收数据的广播*/
    private BroadcastReceiver wxReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(WXEntryActivity.ACTION_GETWX)) {
                wx_access_token = intent.getStringExtra(WXEntryActivity.access_token);
                wx_openid = intent.getStringExtra(WXEntryActivity.openid);
                getWxUserInfo();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thirdlogin);
        initView();
        // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
        // 其中APP_ID是分配给第三方应用的appid如：1106210336，类型为String，我现在用的是官网测试id:222222
        mTencent = Tencent.createInstance("1106210336", this.getApplicationContext());

             /*注册微信广播*/
        IntentFilter filter = new IntentFilter(WXEntryActivity.ACTION_GETWX);
        LocalBroadcastManager.getInstance(this).registerReceiver(wxReceiver, filter);

        boolean netConnect = NetConnectUtil.NetConnect(this);
        if (!netConnect) {
            ToastUtil.showLong("请先将网络打开");
        }

    }

    private void initView() {
        findViewById(R.id.thirdlogin_weixin).setOnClickListener(this);
        findViewById(R.id.thirdlogin_qq).setOnClickListener(this);
        tv = (TextView) findViewById(R.id.thirdlogin_tv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.thirdlogin_weixin:
                wxLogin();
                break;
            case R.id.thirdlogin_qq:
                //如果session无效，就开始做登录操作
                if (!mTencent.isSessionValid()) {
                    qqLogin();
                }
                break;
        }
    }

    /*微信登录*/
    private void wxLogin() {
        //通过WXAPIFactory工厂获取IWXApI的示例
        IWXAPI api = WXAPIFactory.createWXAPI(this, Constant.APP_ID_WX, true);
        //将应用的appid注册到微信
        api.registerApp(Constant.APP_ID_WX);
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
//                req.scope = "snsapi_login";//提示scope参数错误或者没有scope权;限用snsapi_base提示没权限
        req.state = "wechat_sdk_微信登录";
        //用于保持请求和回调的状态，授权请求后原样带回给第三方。
        // 该参数可用于防止csrf攻击（跨站请求伪造攻击），建议第三方带上该参数，可设置为简单的随机数加session进行校验。
        api.sendReq(req);
    }

    /*qq登录*/
    private void qqLogin() {
        //返回数据的监听
        IUiListener qqListen = new IUiListener() {
            @Override
            public void onComplete(Object o) {
                Logs.v(tag + "115 " + o.toString() + "  openId:" + mTencent.getOpenId());
                JSONObject jsonObject = (JSONObject) o;
                try {
                    String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
                    String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
                    String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
                    if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires) && !TextUtils.isEmpty(openId)) {
                        mTencent.setAccessToken(token, expires);
                        mTencent.setOpenId(openId);
                        QQ_uid = openId;
                        getQQMsg();
                    }
                } catch (Exception e) {
                    Logs.e(tag + "128 " + e);
                }
            }

            @Override
            public void onError(UiError uiError) {
                Logs.e(tag + "133 " + uiError.errorDetail);
            }

            @Override
            public void onCancel() {
                ToastUtil.showShort("取消了");
                Logs.d(tag + "140  取消了");
            }
        };
        //注销登录mTencent.logout(this);
   /*     通过这行代码，SDK实现了QQ登录；第二个参出是一个表示获得哪些权限的字符串：
        如:"get userinfo,add t"多个权限用逗号隔开，all代表所有权限。*/
        mTencent.login(Thirdlogin.this, "all", qqListen);
    }

    /*获取个人信息数据*/
    private void getQQMsg() {
        UserInfo userInfo = new UserInfo(this, mTencent.getQQToken());
        userInfo.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object o) {
                Logs.i(tag + "155 " + o.toString());
                tv.setText(o.toString());//打印获取个人信息数据
            }

            @Override
            public void onError(UiError uiError) {
                Logs.e(tag + "160 " + uiError.errorDetail);
            }

            @Override
            public void onCancel() {
                ToastUtil.showShort("取消了");
                Logs.d(tag + "167  取消了");
            }
        });
    }

    /*获取WX的用户信息*/
    private void getWxUserInfo() {
        //https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
        OkHttpUtils.get()
                .url("https://api.weixin.qq.com/sns/userinfo")
                .addParams("access_token", wx_access_token)
                .addParams("openid", wx_openid)//openid:授权用户唯一标识
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        Logs.e(tag + "183 获取错误 " + e + "  " + id + "  " + call);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logs.v(tag + "188 个人信息 " + response + "  " + id);
                    }
                });
    }


}
