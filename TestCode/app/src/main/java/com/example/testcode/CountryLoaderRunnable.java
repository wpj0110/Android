package com.example.testcode;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CountryLoaderRunnable implements Runnable { //load all countries in a hash map

    private final MainActivity mainActivity;

    private static final String dataURL = "https://restcountries.eu/rest/v1/all";

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

        Uri dataUri = Uri.parse(dataURL);
        String urlToUse = dataUri.toString();

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
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


    private ArrayList<Country> parseJSON(String s) {

        ArrayList<Country> countryList = new ArrayList<>();
        try {
            JSONArray jObjMain = new JSONArray(s);

            for (int i = 0; i < jObjMain.length(); i++) {
                JSONObject jCountry = (JSONObject) jObjMain.get(i);
                String name = jCountry.getString("name");


                countryList.add( new Country(name.toUpperCase()));

            }
            return countryList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
