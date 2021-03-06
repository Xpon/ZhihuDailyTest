package com.example.huajie.zhihudailytest;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.IntegerRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.huajie.zhihudailytest.Utils.Constacts;
import com.example.huajie.zhihudailytest.Utils.NewsParseXMLWithJSON;
import com.example.huajie.zhihudailytest.adapter.MyRecyclerViewAdapter;
import com.example.huajie.zhihudailytest.bean.Question;
import com.example.huajie.zhihudailytest.bean.Story;
import com.example.huajie.zhihudailytest.request.HttpRequest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.RecyItemOnclick{
    private static List<Story> newsList = new ArrayList<Story>();
    private List<View> viewList = new ArrayList<View>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        LayoutInflater inflater = getLayoutInflater();
        for (int i = 0; i < Constacts.DAILY_FOR_WEEK; i++) {
            if(i==0){
                View view = inflater.inflate(R.layout.news_list_main, null);
                viewList.add(view);
            }else {
                View view = inflater.inflate(R.layout.news_list, null);
                viewList.add(view);
            }
        }
        sendRequest();
        viewPager.setOffscreenPageLimit(0);
        viewPager.setAdapter(new newsPagerAdapter());

    }

    @Override
    public void onItemOnclick(View v, int position,List<Story> currlist) {
        if(currlist!=null){
            Story story = currlist.get(position);
            if(story.getQuestion().getUrl()!=null&&!story.getQuestion().getUrl().equals("")) {
                Uri uri = Uri.parse(story.getQuestion().getUrl());
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(uri);
                startActivity(intent);
            }
        }
    }

    class newsPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if(position==0){
                View view = viewList.get(position);
                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_main);
                myRecyclerViewAdapter = new MyRecyclerViewAdapter(newsList, position);
                myRecyclerViewAdapter.setRecyItenOnclick(MainActivity.this);
                GridLayoutManager linearLayoutManager = new GridLayoutManager(view.getContext(), 1);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(myRecyclerViewAdapter);
                swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.news_swipe);
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refreshRequest();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                myRecyclerViewAdapter.notifyDataSetChanged();
                                swipeRefreshLayout.setRefreshing(false);

                            }
                        });
                    }
                });
                container.addView(view);
                return viewList.get(position);
            }else {
                View view = viewList.get(position);
                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
                MyRecyclerViewAdapter myRecyclerViewAdapter = new MyRecyclerViewAdapter(newsList, position);
                myRecyclerViewAdapter.setRecyItenOnclick(MainActivity.this);
                GridLayoutManager linearLayoutManager = new GridLayoutManager(view.getContext(), 1);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(myRecyclerViewAdapter);
                container.addView(view);
                return viewList.get(position);
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }
    }
    private void sendRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String currDate = Constacts.Date.getDate();
                    for (int i = 0; i < Constacts.DAILY_FOR_WEEK; i++) {
                        if (i == 0) {
                            List<Story> list = new ArrayList<Story>();
                            URL url = new URL(Constacts.Urls.ZHIHU_DAILY_NEWS);
                            HttpRequest httpRequest = new HttpRequest(url);
                            String data = httpRequest.sendHttpRequest();
                            newsList.addAll(NewsParseXMLWithJSON.parseJSONWithStory(data));
                        }else {
                            List<Story> list = new ArrayList<Story>();
                            int date =Integer.parseInt(currDate);
                            URL url = new URL(Constacts.Urls.ZHIHU_DAILY_BEFORE+(date-i));
                            HttpRequest httpRequest = new HttpRequest(url);
                            String data = httpRequest.sendHttpRequest();
                            newsList.addAll(NewsParseXMLWithJSON.parseJSONWithStory(data));
                        }
                    }
                    for(int i= 0;i<newsList.size();i++){
                        Story story = newsList.get(i);
                        Log.e("zhihuquestion","id="+story.getId());
                        URL questionUrl = new URL(Constacts.Urls.ZHIHU_DAILY_OFFLINE + story.getId());
                        HttpRequest httpRequest = new HttpRequest(questionUrl);
                        String questionData = httpRequest.sendHttpRequest();
                        Question question = NewsParseXMLWithJSON.parseQusetionWithJsoup(questionData);
                        if(question!=null) {
                            Log.e("zhihuquestion","title="+question.getTitle());
                            story.setQuestion(question);
                        }
                    }
                }catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void refreshRequest(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    List<Story> list = new ArrayList<Story>();
                    URL url = new URL(Constacts.Urls.ZHIHU_DAILY_NEWS);
                    HttpRequest httpRequest = new HttpRequest(url);
                    String data = httpRequest.sendHttpRequest();
                    List<Story> sortList=new ArrayList<Story>();
                    Collections.sort(newsList);
                    String maxId = newsList.get(0).getId();
                    for(Story story:NewsParseXMLWithJSON.parseJSONWithStory(data)){
                        if(Integer.parseInt(story.getId())> Integer.parseInt(maxId)) {
                            sortList.add(story);
                        }
                    }
                    for(int i= 0;i<sortList.size();i++){
                        Story story = sortList.get(i);
                        URL questionUrl = new URL(Constacts.Urls.ZHIHU_DAILY_OFFLINE + story.getId());
                        HttpRequest httpRequest1 = new HttpRequest(questionUrl);
                        String questionData = httpRequest1.sendHttpRequest();
                        Question question = NewsParseXMLWithJSON.parseQusetionWithJsoup(questionData);
                        if(question!=null) {
                            story.setQuestion(question);
                        }
                    }
                    newsList.addAll(sortList);
                    Collections.sort(newsList);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
