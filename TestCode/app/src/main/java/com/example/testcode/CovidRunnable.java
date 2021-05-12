package com.example.testcode;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CovidRunnable implements Runnable{

    private static final String TAG = "CovidRunnableTAG";
    private static String country;


    public CovidRunnable(MainActivity mainActivity, String country){
        CovidRunnable.country = country;
    }

    //private void processResults(String s) {
    //    final ArrayList<News> newsList = parseJSON(s);
    //    if (newsList != null)  {
    //        mainActivity.runOnUiThread(() -> mainActivity.updateData(newsList));
    //    }
    //}

    public void run(){

        //From: https://rapidapi.com/Gramzivi/api/covid-19-data
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://covid-19-data.p.rapidapi.com/country?name="+country+"&format=json")
                .get()
                .addHeader("x-rapidapi-key", "7c1287bb3fmshac0400191dbdc6bp1a134ajsn3ac938e6fbb7")
                .addHeader("x-rapidapi-host", "covid-19-data.p.rapidapi.com")
                .build();


        //Guide: https://codinginflow.com/tutorials/android/okhttp-simple-get-request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    Log.d(TAG, "onResponse: "+myResponse);
                    //parseJsonHere
                    parseJSON(myResponse);
                }
            }
        });
        //processResults(sb.toString());
    }



    private ArrayList<Covid> parseJSON(String s) {
        Log.d(TAG, "parseJSON: String is: "+s);

        ArrayList<Covid> covidList = new ArrayList<>();
        try {
            Log.d(TAG, "parseJSON: inside try");

            JSONArray jArr = new JSONArray(s); //it is a json array, and the free version only returns one object
            JSONObject jObj = jArr.getJSONObject(0);
            Log.d(TAG, "parseJSON: jObj 0 is "+jObj);

            String country = jObj.getString("country");
            String confirmed = jObj.getString("confirmed");
            String recovered = jObj.getString("recovered");
            String critical = jObj.getString("critical");
            String deaths = jObj.getString("deaths");
            String lastChange = jObj.getString("lastChange");
            String lastUpdate = jObj.getString("lastUpdate");


            covidList.add(new Covid(country,confirmed,recovered,critical,deaths,lastChange,lastUpdate));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
