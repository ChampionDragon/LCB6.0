package com.lcb.one.http;

import com.lcb.one.util.Logs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpByPost {
    public static String executeHttpPost(String data, String urlStr) {
        String result = "NO Result";
        URL url = null;
        HttpURLConnection connection = null;
        InputStreamReader in = null;
        PrintWriter pw = null;
        try {
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            connection.setRequestProperty("Charset", "utf-8");
            connection.setRequestProperty("connection", "Keep-Alive");
//          connection.setRequestProperty("User-Agent",
//          "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; .NET CLR 3.0.04506;SV1)");
            connection.setRequestProperty("accept", "*/*");

            int responseCode = connection.getResponseCode();
            String responseMessage = connection.getResponseMessage();
            Logs.d("httppost38     code: " + responseCode + "     msg:  " + responseMessage);

            pw = new PrintWriter(connection.getOutputStream());
            pw.print(data);
            pw.flush();
            in = new InputStreamReader(connection.getInputStream());
            Logs.w(in.toString() + "    " + connection.getInputStream().toString());
            BufferedReader bufferedReader = new BufferedReader(in);
            StringBuffer strBuffer = new StringBuffer();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                strBuffer.append(line);
            }
            result = strBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Logs.e("httppost57   " + e);
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
            if (pw != null) {
                pw.close();
            }

        }
        return result;
    }
}
