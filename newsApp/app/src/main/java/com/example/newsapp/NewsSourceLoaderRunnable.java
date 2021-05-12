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

public class NewsSourceLoaderRunnable implements Runnable{ //These are the sources section (CNN, ABC, NBC, FOX, etc)

    private static final String TAG = "SourceLoaderTAG";

    private final MainActivity mainActivity;

    private static final String categoryPart1 = "https://newsapi.org/v2/sources?language=en&country=us&category=";
    private static final String categoryPart2 = "&apiKey=744ea238ad79411cb539787c806a3cf3";
    private static String dataURL;
    private static String category;

    public NewsSourceLoaderRunnable(MainActivity ma, String category) {
        Log.d(TAG, "NewsSourceLoaderRunnable: ");
        this.mainActivity = ma;
        this.category = category;
    }

    private void processResults(String s) {
        final ArrayList<News> newsList = parseJSON(s);
        if (newsList != null)  {
            mainActivity.runOnUiThread(() -> mainActivity.updateData(newsList));
        }
    }

    public void run(){
        if (category == null || category.length() == 0 || category.equals("All")) {
            category = ""; //initial application open is empty, or if category selected is All
        }
        dataURL = categoryPart1 + category + categoryPart2;
        //dataURL = "https://www.google.com";
        //dataURL = "https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyC80iYlZPKSgn0JGFo2U9FSa1hs01npzfs&address=";
        Log.d(TAG, "run: dataURL = "+ dataURL);
        Uri dataUri = Uri.parse(dataURL);
        String urlToUse = dataUri.toString();

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn = (HttpURLConnection) url.openConnection();
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

        processResults(sb.toString());

    }

    private ArrayList<News> parseJSON(String s) {
        Log.d(TAG, "parseJSON: String is: "+s);

        ArrayList<News> newsList = new ArrayList<>();
        try {
            Log.d(TAG, "parseJSON: inside try");
            JSONObject jObjMain = new JSONObject(s);
            JSONObject jSources = jObjMain;
            JSONArray sourcesArr = jSources.getJSONArray("sources");
            Log.d(TAG, "parseJSON: array is "+sourcesArr);


            for (int i = 0; i < sourcesArr.length(); i++) {

                String id = sourcesArr.getJSONObject(i).getString("id");
                String name = sourcesArr.getJSONObject(i).getString("name");
                //Log.d(TAG, "parseJSON: name is: "+name);
                String category = sourcesArr.getJSONObject(i).getString("category");


                newsList.add(new News(id,name,category));

            }
            return newsList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
