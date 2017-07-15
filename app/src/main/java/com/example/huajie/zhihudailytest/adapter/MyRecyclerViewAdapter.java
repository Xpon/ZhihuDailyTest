package com.example.huajie.zhihudailytest.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.huajie.zhihudailytest.R;
import com.example.huajie.zhihudailytest.Utils.Constacts;
import com.example.huajie.zhihudailytest.bean.Story;
import com.example.huajie.zhihudailytest.request.HttpRequest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HuaJie on 2017/7/12.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    private RecyItemOnclick recyItemOnclick;
    private Context mContext;
    private List<Story> mNewsList=null;
    private int newsPager;
    public MyRecyclerViewAdapter(List<Story> list,int pager){
        mNewsList=list;
        newsPager=pager;
    }
    public interface RecyItemOnclick{
        public void onItemOnclick(View v , int position ,List<Story> currList);
    }
    public void setRecyItenOnclick(RecyItemOnclick recyItemOnclick){
        this.recyItemOnclick=recyItemOnclick;
    }

     class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cardView;
        ImageView news_image;
        TextView news_title;
        TextView news_summary;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView= (CardView) itemView;
            news_image= (ImageView) cardView.findViewById(R.id.news_image);
            news_title= (TextView) cardView.findViewById(R.id.news_title);
            news_summary= (TextView) cardView.findViewById(R.id.news_summary);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position =getPosition();
            recyItemOnclick.onItemOnclick(v,position,getCurrentNews());
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext==null) {
            mContext = parent.getContext();
        }
        View view=LayoutInflater.from(mContext).inflate(R.layout.news_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Story story =getCurrentNews().get(position);
        holder.news_title.setText(story.getQuestion().getTitle());
        holder.news_summary.setText(story.getTitle());
        try {
            Glide.with(mContext).load(new URL(story.getImageUrl())).into(holder.news_image);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return getCurrentNews().size();
    }


    private List<Story> getCurrentNews(){
        List<Story> currList = new ArrayList<Story>();
        int index=Integer.parseInt(Constacts.Date.getDate());
        String currentDate= String.valueOf(index-newsPager-1);
        currList.clear();
        for(Story story:mNewsList){
            if(currentDate.equals(story.getDate())){
                if(story.getQuestion()!=null&&story.getQuestion().getTitle()!=null&&!story.getQuestion().getTitle().equals("")){
                    currList.add(story);
                }
            }
        }
        return currList;
    }
}
