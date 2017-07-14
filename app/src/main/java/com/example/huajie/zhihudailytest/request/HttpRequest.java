package com.example.huajie.zhihudailytest.request;


import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by HuaJie on 2017/7/12.
 */

public class HttpRequest {
    private URL newsURL;
    private HttpURLConnection conn;
    private BufferedReader br;
    public HttpRequest(URL url){
        newsURL = url;
    }
    public String sendHttpRequest() {
        try {
            conn = (HttpURLConnection) newsURL.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);
            InputStream in = conn.getInputStream();
            br = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            Log.e("date","respones="+response);
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }
}
