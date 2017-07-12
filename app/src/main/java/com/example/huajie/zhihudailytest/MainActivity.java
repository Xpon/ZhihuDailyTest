package com.example.huajie.zhihudailytest;

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
import android.widget.TextView;

import com.example.huajie.zhihudailytest.adapter.MyRecyclerViewAdapter;
import com.example.huajie.zhihudailytest.bean.News;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<News> newsList = new ArrayList<News>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        LayoutInflater inflater = getLayoutInflater();
        final List<View> viewList = new ArrayList<View>();
        for(int i=0;i<7;i++){
            News news =new News(i+"hua",i+"hua",i+"hua");
            newsList.add(news);
            View view = inflater.inflate(R.layout.news_list,null);
            viewList.add(view);
        }
        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                Log.e("huajie","size="+viewList.size());
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = viewList.get(position);
                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
                MyRecyclerViewAdapter myRecyclerViewAdapter = new MyRecyclerViewAdapter(newsList);
                GridLayoutManager linearLayoutManager = new GridLayoutManager(view.getContext(),1);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(myRecyclerViewAdapter);
                container.addView(view);
                return viewList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(viewList.get(position));
            }
        };
        viewPager.setAdapter(pagerAdapter);


    }

}
