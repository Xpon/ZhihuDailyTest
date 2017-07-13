package com.example.huajie.zhihudailytest;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.huajie.zhihudailytest.Utils.Constacts;
import com.example.huajie.zhihudailytest.Utils.NewsParseXMLWithJSON;
import com.example.huajie.zhihudailytest.adapter.MyRecyclerViewAdapter;
import com.example.huajie.zhihudailytest.bean.News;
import com.example.huajie.zhihudailytest.bean.Story;
import com.example.huajie.zhihudailytest.request.HttpRequest;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.RecyItemOnclick{
    private List<Story> newsList = new ArrayList<Story>();
    private List<View> viewList = new ArrayList<View>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        LayoutInflater inflater = getLayoutInflater();
        for (int i = 0; i < 7; i++) {
            View view = inflater.inflate(R.layout.news_list, null);
            viewList.add(view);
        }
        newsList=NewsParseXMLWithJSON.stories;
        viewPager.setAdapter(new newsPagerAdapter());
    }

    @Override
    public void onItemOnclick(View v, int position) {
        if(newsList!=null){
            Story story = newsList.get(position);
            String id = story.getId();
            Uri uri = Uri.parse("https://www.zhihu.com/question/" + id);
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setData(uri);
            startActivity(intent);
        }
    }

    class newsPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            Log.e("huajie", "size=" + viewList.size());
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.e("huajie","newsList.size="+newsList.size());
            Log.e("huajie","viewList.size="+viewList.size());

            try {
                View view = viewList.get(position);
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR,1-position);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
                String date = simpleDateFormat.format(calendar.getTime());
                Log.e("date","date="+date+";"+"position="+position);
                if(position==0) {
                    newsList.clear();
                    sendRequest(new URL(Constacts.Urls.ZHIHU_DAILY_NEWS));
                }else {
                    newsList.clear();
                    sendRequest(new URL(Constacts.Urls.ZHIHU_DAILY_BEFORE + date));
                }
                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
                MyRecyclerViewAdapter myRecyclerViewAdapter = new MyRecyclerViewAdapter(newsList);
                myRecyclerViewAdapter.setRecyItenOnclick(MainActivity.this);
                GridLayoutManager linearLayoutManager = new GridLayoutManager(view.getContext(), 1);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(myRecyclerViewAdapter);
                container.addView(view);
                return viewList.get(position);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }
        private void sendRequest(final URL url) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.e("huajie","new Thread");
                    HttpRequest httpRequest = new HttpRequest(url);
                    String data = httpRequest.sendHttpRequest();
                    NewsParseXMLWithJSON.parseJSONWithStory(data);
                }
            }).start();
        }
    }

}
