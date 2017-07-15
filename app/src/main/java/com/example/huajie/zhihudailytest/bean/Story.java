package com.example.huajie.zhihudailytest.bean;

import android.support.annotation.NonNull;

import java.net.URL;

/**
 * Created by HuaJie on 2017/7/13.
 */

public class Story implements Comparable {
    private String date;
    private String title;
    private String imageUrl;
    private String id;
    private Question question;

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        Story story = (Story) o;
        if(this.id.equals(story.id)){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Integer.parseInt(this.id);
    }

    @Override
    public int compareTo(Object o) {
        Story story = (Story) o;
        return story.id.compareTo(this.id);
    }
}
