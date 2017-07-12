package com.example.huajie.zhihudailytest.bean;

/**
 * Created by HuaJie on 2017/7/12.
 */

public class News {
    private String newsTitle;
    private String newsSummary;
    private String newsImage;
    public News(String newsTitle,String newsSummary,String newsImage){
        this.newsTitle=newsTitle;
        this.newsSummary=newsSummary;
        this.newsImage=newsImage;
    }
    public String getNewsTitle() {
        return newsTitle;
    }

    public String getNewsSummary() {
        return newsSummary;
    }

    public String getNewsImage() {
        return newsImage;
    }

}
