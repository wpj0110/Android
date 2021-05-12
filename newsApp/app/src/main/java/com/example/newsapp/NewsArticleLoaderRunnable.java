package com.example.newsapp;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class NewsArticleLoaderRunnable implements Runnable{

    private static final String TAG = "ArticleLoaderTAG";

    private final NewsService newsService;
    private final  String selectedSource;

    NewsArticleLoaderRunnable(NewsService newsService, String selectedSource){
        this.newsService = newsService;
        this.selectedSource = selectedSource;
    }

    @Override
    public void run() {
        String url1 = "https://newsapi.org/v2/top-headlines?sources=";
        String url2 = "&language=en&apiKey=744ea238ad79411cb539787c806a3cf3";
        String dataURL = url1 + selectedSource + url2;
        Uri dataUri = Uri.parse(dataURL);
        String urlToUse = dataUri.toString();

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.addRequestProperty("User-Agent","");

            if (conn.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                Log.d(TAG, "run: HTTP ResponseCode NOT OK: " + conn.getResponseCode());
                return;
            }

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }


        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        final ArrayList<NewsArticles> aList = parseJSON(sb.toString());
        newsService.setArticles(aList);
    }
    private ArrayList<NewsArticles> parseJSON(String s) {
        Log.d(TAG, "parseJSON: String is: "+s);

        ArrayList<NewsArticles> articleList = new ArrayList<>();
        try {
            Log.d(TAG, "parseJSON: inside try");
            JSONObject jObjMain = new JSONObject(s);
            JSONObject jSources = jObjMain;
            JSONArray articlesArr = jSources.getJSONArray("articles");
            Log.d(TAG, "parseJSON: array is "+articlesArr);


            int listSize;
            if (articlesArr.length() > 10){
                listSize = 10;
            } else{
                listSize = articlesArr.length();
            }
            for (int i = 0; i < listSize; i++) {

                String author = articlesArr.getJSONObject(i).getString("author");
                String title = articlesArr.getJSONObject(i).getString("title");
                String description = articlesArr.getJSONObject(i).getString("description");
                String url = articlesArr.getJSONObject(i).getString("url");
                String urlToImage = articlesArr.getJSONObject(i).getString("urlToImage");
                String publishedAt = articlesArr.getJSONObject(i).getString("publishedAt");

                //    NewsArticles(String author, String title, String description, String url, String urlToImage, String publishedAt)
                articleList.add(new NewsArticles(author,title,description,url,urlToImage,publishedAt));
            }
            return articleList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
