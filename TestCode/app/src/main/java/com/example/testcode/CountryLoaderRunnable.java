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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CountryLoaderRunnable implements Runnable {

    private final MainActivity mainActivity;
    private final static String TAG = "CountryRunnableTAG";

    public CountryLoaderRunnable(MainActivity ma) {
        mainActivity = ma;
    }

    private void processResults(String s) {
        final ArrayList<Country> countryList = parseJSON(s);
        if (countryList != null)  {
            mainActivity.runOnUiThread(() -> mainActivity.updateCountry(countryList));
        }
    }

    public void run() {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://covid-193.p.rapidapi.com/countries")
                .get()
                .addHeader("x-rapidapi-key", "7c1287bb3fmshac0400191dbdc6bp1a134ajsn3ac938e6fbb7")
                .addHeader("x-rapidapi-host", "covid-193.p.rapidapi.com")
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
                    processResults(myResponse);
                }
            }
        });

        //processResults(sb.toString());
    }


    private ArrayList<Country> parseJSON(String s) {

        ArrayList<Country> countryList = new ArrayList<>();
        try {
            //JSONArray jObjMain = new JSONArray(s);
            JSONObject jObjMain = new JSONObject(s);
            JSONArray countryArr = jObjMain.getJSONArray("response");
            //Log.d(TAG, "parseJSON: countryArr "+countryArr);

            for (int i = 0; i < countryArr.length(); i++) {
                //JSONObject tempObj = countryArr.getJSONObject(i);
                //String name = tempObj.getString(""+i);
                String name = countryArr.get(i).toString();
                //Log.d(TAG, "parseJSON: country name = "+name);
                countryList.add( new Country(name.toUpperCase()));

            }
            return countryList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
