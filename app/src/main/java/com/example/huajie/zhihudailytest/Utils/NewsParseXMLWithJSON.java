package com.example.huajie.zhihudailytest.Utils;

import android.util.Log;

import com.example.huajie.zhihudailytest.bean.Question;
import com.example.huajie.zhihudailytest.bean.Story;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Created by HuaJie on 2017/7/13.
 */

public class NewsParseXMLWithJSON {



    public static List<Story> parseJSONWithStory(String jsonData){
        try {
            List<Story> stories = new ArrayList<>();
            if(jsonData!=null) {
                JSONObject jsonObject = new JSONObject(jsonData);
                String date = jsonObject.getString("date");
                if (jsonObject.has("top_stories")) {
                    JSONArray jsonArray_top = jsonObject.getJSONArray("top_stories");
                    for (int i = 0; i < jsonArray_top.length(); i++) {
                        Story top_story = new Story();
                        JSONObject jsonObject_top = jsonArray_top.getJSONObject(i);
                        top_story.setImageUrl(jsonObject_top.getString("image"));
                        top_story.setId(jsonObject_top.getString("id"));
                        top_story.setTitle(jsonObject_top.getString("title"));
                        top_story.setDate(date);
                        stories.add(top_story);
                        Log.e("huajie", "stories.size=" + stories.size());
                    }
                }

                JSONArray jsonArray = jsonObject.getJSONArray("stories");
                for (int i = 0; i < jsonArray.length(); i++) {
                    Story story = new Story();
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    JSONArray jsonArray_iamge = jsonObject1.getJSONArray("images");
                    story.setImageUrl((String) jsonArray_iamge.get(0));
                    story.setId(jsonObject1.getString("id"));
                    story.setTitle(jsonObject1.getString("title"));
                    story.setDate(date);
                    stories.add(story);
                }
                HashSet hashSet = new HashSet(stories);
                stories.clear();
                stories.addAll(hashSet);
                Collections.sort(stories);
                return stories;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Question parseQusetionWithJsoup(String questionData){
        if(questionData!=null) {
            try {
                Question question = new Question();
                JSONObject jsonObject = new JSONObject(questionData);
                String body = jsonObject.getString("body");
                String id = jsonObject.getString("id");
                Document document = Jsoup.parse(body);
                Element titleElement = document.getElementsByClass("question-title").first();
                Element element = document.getElementsByClass("view-more").first();
                if(titleElement!=null&&element!=null) {
                    String title = titleElement.text();
                    Document urlDoc = Jsoup.parse(element.toString());
                    Element urlElement = urlDoc.select("a").first();
                    String url = urlElement.attr("href");
                    question.setTitle(title);
                    question.setUrl(url);
                    question.setId(id);
                    return question;
                }
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
