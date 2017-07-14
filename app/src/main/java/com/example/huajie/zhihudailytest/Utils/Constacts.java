package com.example.huajie.zhihudailytest.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by HuaJie on 2017/7/13.
 */

public class Constacts {
    public static final int DAILY_FOR_WEEK = 7;

    public static final class Urls{
        public static final String ZHIHU_DAILY_NEWS = "https://news-at.zhihu.com/api/4/news/latest";
        public static final String ZHIHU_DAILY_BEFORE = "http://news.at.zhihu.com/api/4/news/before/";
        public static final String ZHIHU_DAILY_OFFLINE="https://news-at.zhihu.com/api/4/news/";
    }
    public static final class Date{
        public static final String getDate(){
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR,1);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            String date = simpleDateFormat.format(calendar.getTime());
            return date;
        }


    }
}
