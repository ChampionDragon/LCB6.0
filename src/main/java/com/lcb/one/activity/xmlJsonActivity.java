package com.lcb.one.activity;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lcb.one.R;
import com.lcb.one.base.BaseActivity;
import com.lcb.one.constant.Constant;
import com.lcb.one.listener.PermissionListener;
import com.lcb.one.util.Logs;
import com.lcb.one.util.NetConnectUtil;
import com.lcb.one.util.ToastUtil;
import com.lcb.one.view.DialogLoading;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.arnaudguyon.xmltojsonlib.JsonToXml;
import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import okhttp3.Call;

/**
 * Description:xml和json互相转换,文件上传下载
 * AUTHOR: Champion Dragon
 * created at 2019/5/28
 **/
public class xmlJsonActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv;
    private String xmlString;
    private DialogLoading dialoading;
    private int myprogress;//进度条数值


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xml_json);
        initView();
        askForPermission();
    }

    private void initView() {
        tv = findViewById(R.id.xmljson_tv);
        findViewById(R.id.back_xmljson).setOnClickListener(this);
        findViewById(R.id.xtoj).setOnClickListener(this);
        findViewById(R.id.jtox).setOnClickListener(this);
        findViewById(R.id.download).setOnClickListener(this);
        findViewById(R.id.upload).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_xmljson:
                finish();
                break;
            case R.id.xtoj:
                Xml2Json();
                break;
            case R.id.jtox:
                Json2Xml();
                break;
            case R.id.upload:
                upLoad();
                break;
            case R.id.download:
                downLoad();
                break;
        }
    }

    /*下载文件*/
    private void downLoad() {
        dialoading = new DialogLoading(xmlJsonActivity.this, "准备下载");
        dialoading.show();
        executor.submit(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get().url("http://gyxz.ukdj3d.cn/a31/rj_zmy1/szcsd.apk").build().execute(new FileCallBack(
                        Constant.fileLCB.getAbsolutePath(), "1.apk") {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        if (!NetConnectUtil.NetConnect(xmlJsonActivity.this)) {
                            ToastUtil.showLong("未连接到网络,文件下载失败");
                        }
                        Logs.e("91 " + e + "  " + i);
                        dialoading.close();
                    }

                    @Override
                    public void onResponse(File file, int i) {
                        dialoading.setTv("下载完成");
                        dialoading.close();
                        Logs.v(" 99 " + file.getAbsolutePath() + "  " + i);
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        myprogress = (int) (progress * 100);
                        dialoading.setTv("下载进度：" + myprogress + "%");
                    }
                });
            }
        });
    }

    /*上传文件*/
    private void upLoad() {
        dialoading = new DialogLoading(xmlJsonActivity.this, "开始上传");
        dialoading.show();
        /*设置通用的请求属性    content-type 指示响应内容的格式   content-disposition 指示如何处理响应内容。*/
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Disposition", "form-data;filename=enctype");
        headers.put("Content-Type", "application/x-www-form-urlencoded");// 用于定义网络文件的类型和网页的编码
        /*请求所需的key和value数据*/
        Map<String, String> params = new HashMap<>();
        params.put("username", "lcb");
        params.put("password", "123456");
        File file = new File(Constant.fileLCB.getAbsolutePath(), "1.apk");
        if (!file.exists()) {
            ToastUtil.showLong("文件不存在，请修改文件路径");
            return;
        }
        String filename = file.getName();
        OkHttpUtils.post().params(params).url("https://blog.csdn.net/g984160547/article/details/74909716")
                .headers(headers)
                .addFile("mFile", filename, file) //支持传输多个文件只需要一直累加添加addFile
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        myprogress = (int) (progress * 100);
                        if (progress == 1) {
                            dialoading.setTv("上传完成");
                        } else {
                            dialoading.setTv("上传进度：" + myprogress + "%");
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (!NetConnectUtil.NetConnect(xmlJsonActivity.this)) {
                            ToastUtil.showLong("未连接到网络,文件下载失败");
                        }
                        Logs.e("154 " + e + "  " + id);
                        dialoading.close();
                    }

                    @Override
                    public void onResponse(String response, int id) {
//                        dialoading.setTv("上传完成");
                        dialoading.close();
                    }
                });
    }

    /*json转xml*/
    private void Json2Xml() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("lcb", 1);
            jsonObject.put("a", "da");
            jsonObject.put("das", "saa");
            jsonObject.put("tes", "tsda");
            jsonObject.put("ko", "dsadas");
        } catch (JSONException e) {
            Logs.e("62" + e);
        }

        JsonToXml jsonToXml = new JsonToXml.Builder(jsonObject).build();
        xmlString = jsonToXml.toString();
        tv.setText("xml数据:" + xmlString);
        Logs.v(xmlString);
        Logs.i("修改后xml数据:" + xmlString.replace("utf-8", "GBK").replace("standalone='yes' ", ""));
    }

    /*xml转json*/
    private void Xml2Json() {
        if (xmlString == null) {
            ToastUtil.showLong("先按“json转xml”");
        } else {
            XmlToJson xmlToJson = new XmlToJson.Builder(xmlString).build();
            tv.setText("正常json数据:" + xmlToJson.toString() + "\n 格式化json数据:" + xmlToJson.toFormattedString() +
                    "\n 格式化分行json数据:" + xmlToJson.toFormattedString("ada"));//要使用的缩进，例如“”或“\ t”。
        }

    }


    /*动态申请权限:创建文件夹需要WRITE_EXTERNAL_STORAGE*/
    private void askForPermission() {
        requestRunPermisssion(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
            @Override
            public void onGranted() {

            }

            @Override
            public void onDenied(List<String> deniedPermission) {
                String s = "被拒绝的权限：";
                for (String permission : deniedPermission) {
                    s += permission + "\n";
                }
                Toast.makeText(xmlJsonActivity.this, s, Toast.LENGTH_SHORT).show();
                //提示用户到系统去设置权限
                msg_one();
            }
        });
    }


}
