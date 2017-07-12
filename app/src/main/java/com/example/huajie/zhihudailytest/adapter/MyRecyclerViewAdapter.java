package com.example.huajie.zhihudailytest.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huajie.zhihudailytest.R;
import com.example.huajie.zhihudailytest.bean.News;

import java.util.List;

/**
 * Created by HuaJie on 2017/7/12.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    private Context mContext;
    private List<News> mNewsList;
    public MyRecyclerViewAdapter(List<News> list){
        mNewsList=list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
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
        News news=mNewsList.get(position);
        holder.news_title.setText(news.getNewsTitle());
        holder.news_summary.setText(news.getNewsSummary());
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }
}
