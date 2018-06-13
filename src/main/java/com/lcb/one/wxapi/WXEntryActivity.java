package com.lcb.one.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.lcb.one.base.BaseActivity;
import com.lcb.one.constant.Constant;
import com.lcb.one.util.Logs;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Description: 微信登录的回调页面
 * AUTHOR: Champion Dragon
 * created at 2018/6/13
 **/
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    /**
     * 微信登录相关
     */
    private IWXAPI api;
    private String tag = "WXEntryActivity";
    public static String access_token = "access_token";
    public static String openid = "openid";
    public static final String ACTION_GETWX = "com.bs.utown.get_wx_msg";//用于广播的action


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //通过WXAPIFactory工厂获取IWXApI的示例
        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID_WX, true);
        //将应用的appid注册到微信
        api.registerApp(Constant.APP_ID_WX);
        //注意：
        //第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，如果返回值为false，
        // 则说明入参不合法未被SDK处理，应finish当前透明界面，避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
        try {
            boolean result = api.handleIntent(getIntent(), this);
            if (!result) {
                Logs.i("参数不合法，未被SDK处理，退出");
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        api.handleIntent(data, this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        finish();
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Logs.d(tag + "80   baseReq:" +baseReq);
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Logs.i(tag + "86  " + baseResp.errStr + "," + baseResp.openId + "," + baseResp.transaction + "," + baseResp.errCode);

        String code = ((SendAuth.Resp) baseResp).code;
        String result = "";
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "发送成功";
/*现在请求获取数据 access_token https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code*/

                OkHttpUtils.get().url("https://api.weixin.qq.com/sns/oauth2/access_token")
                        .addParams("appid", Constant.APP_ID_WX)
                        .addParams("secret", Constant.APP_SECRET_WX)
                        .addParams("code", code)
                        .addParams("grant_type", "authorization_code")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(okhttp3.Call call, Exception e, int id) {
                                Logs.e(tag + "113 请求错误.." + e + "  " + id + " \n " + call);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Logs.v(tag + "118 response:" + response);
                                String tokenstr = null, openidstr = null;
                                try {
                                    JSONObject jo = new JSONObject(response);
                                    tokenstr = jo.getString(access_token);
                                    openidstr = jo.getString(openid);
                                    Logs.i(tag + " 116  " + tokenstr + "   " + openidstr);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                /*通过广播将access_token和openid返回登录界面*/
                                LocalBroadcastManager.getInstance(WXEntryActivity.this).sendBroadcast(new Intent(ACTION_GETWX).
                                        putExtra(access_token, tokenstr).putExtra(openid, openidstr));
                            }
                        });
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "发送取消";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "发送被拒绝";
                break;
            case BaseResp.ErrCode.ERR_BAN:
                result = "签名错误";
                break;
            default:
                result = "发送返回";
                break;
        }
        Toast.makeText(WXEntryActivity.this, result, Toast.LENGTH_LONG).show();
        finish();
    }

}