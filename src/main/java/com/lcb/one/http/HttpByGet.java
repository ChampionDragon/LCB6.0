package com.lcb.one.http;

import android.text.TextUtils;
import android.util.Log;

import com.lcb.one.util.Logs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class HttpByGet {
    public static String result = "HttpByGet__NO Result";
    public static String error = "HttpByGet__error";
    static String tag = "HttpByGet";

    public static String executeHttpGet(String urlStr) {
        URL url = null;
        HttpURLConnection connection = null;
        InputStreamReader in = null;
        try {
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            // connection.setRequestProperty("user-agent",
            // "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");// 用于定义网络文件的类型和网页的编码
            connection.setReadTimeout(20 * 1000);
            connection.setConnectTimeout(20 * 1000);
            // 建立实际的连接
            connection.connect();

            int responseCode = connection.getResponseCode();
            String responseMessage = connection.getResponseMessage();
            Log.d("lcb", "httpbyget34   code: " + responseCode + "     msg:  "
                    + responseMessage);

            in = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(in);
            StringBuffer strBuffer = new StringBuffer();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                strBuffer.append(line);
            }
            result = strBuffer.toString();
        } catch (Exception e) {
            error = e.toString();
            result = error;
            Logs.d("httpbyget46   " + e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }

    /**
     * 返回自定义url
     */
    public static String get(String... b) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            if ((i + 1) % 2 == 0) {
                if ((i + 1) == b.length)
                    sb.append("=" + b[i]);
                else {
                    sb.append("=" + b[i] + "&");
                }
            } else {
                sb.append(b[i]);
            }

        }
        return "?" + sb.toString();
    }

    public static String getUrl(String url, String... b) {
        String urlstr;
        StringBuffer sb = new StringBuffer();
        if (!url.contains("?")) {
            sb.append("?");
        }
        for (int i = 0; i < b.length; i++) {
            try {
                b[i] = URLEncoder.encode(b[i], "utf-8");
            } catch (UnsupportedEncodingException e) {
                Logs.d(tag + " 102 " + e);
            }
            if ((i + 1) % 2 == 0) {
                if ((i + 1) == b.length)
                    sb.append("=" + b[i]);
                else {
                    sb.append("=" + b[i] + "&");
                }
            } else {
                sb.append(b[i]);
            }
        }
        Logs.d(tag + " 109 " + sb);
        urlstr = url + sb.toString();
        return urlstr;

    }

    /*通过map拼接返回URL*/
    public static String genUrl(String url, Map<String, String> params) {
        if (params == null || params.size() == 0) {
            return url;
        }
        StringBuilder urlBuilder = new StringBuilder(url);
        int versionCode = 2410;
        if (!url.contains("?")) {
            urlBuilder.append("?p=android&v=" + versionCode);
        } else {
            if (url.endsWith("?")) {
                urlBuilder.append("p=android&v=" + versionCode);
            } else {
                urlBuilder.append("&p=android&v=" + versionCode);
            }
        }
        String value;
        for (String key : params.keySet()) {
            urlBuilder.append("&");
            urlBuilder.append(key);
            urlBuilder.append("=");
            value = params.get(key);
            try {
                if (!TextUtils.isEmpty(value)) {
                    value = URLEncoder.encode(value, "UTF-8");
                }
            } catch (UnsupportedEncodingException e) {
                // Do nothing
                e.printStackTrace();
            }
            urlBuilder.append(value);
        }
        return urlBuilder.toString();
    }


}
