package com.example.civiladvocacyapp;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class OffLoaderRunnable implements Runnable {

    private static final String TAG = "OffLoaderRunnableTag";
    private final MainActivity mainActivity;
    private String location;
    public static String addr;
    private static final String DATA_URL = "https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyC80iYlZPKSgn0JGFo2U9FSa1hs01npzfs&address=";
    OffLoaderRunnable(MainActivity mainActivity, String location) {
        this.mainActivity = mainActivity;
        this.location = location;
    }


    @Override
    public void run() {
        //location = "Chicago,IL"; //temporary location, debugging purposes
        Uri dataUri = Uri.parse(DATA_URL+location);
        String urlToUse = dataUri.toString();
        Log.d(TAG, "run: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            if (conn.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                Log.d(TAG, "run: HTTP ResponseCode NOT OK: " + conn.getResponseCode());
                handleResults(null);
                return;
            }

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "run: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "run: ", e);
            handleResults(null);
            return;
        }

        handleResults(sb.toString());

    }

    private void handleResults(String s) {

        if (s == null) {
            Log.d(TAG, "handleResults: Failure in data download");
            mainActivity.runOnUiThread(mainActivity::downloadFailed);
            return;
        }

        final ArrayList<Official> officialList = parseJSON(s);
        mainActivity.runOnUiThread(() -> {
            if (officialList != null)
                //Toast.makeText(mainActivity, "Loaded " + officialList.size() + " symbols.", Toast.LENGTH_LONG).show();
            mainActivity.updateData(officialList); //one of the most crucial parts
        });
    }

    private ArrayList<Official> parseJSON(String s) {
        addr = "";
        StringBuilder sb = new StringBuilder();

        ArrayList<Official> officialList = new ArrayList<>();
        try {
            //JSONArray jObjMain = new JSONArray(s);
            JSONObject jObjMain = new JSONObject(s);
            Log.d(TAG, "parseJSON: jObjMain.length() = "+jObjMain.length());
            //////////////////////////////////////////////////////////////////////////////////FOR LOOP
            //for (int i = 0; i < jObjMain.length(); i++) { //this is problematic, length is 5 always.
            for (int i = 0; i < 10; i++) { //I hope it doesn't crash when there are less than 10 officials, but so far this works.
                JSONObject jOfficial = jObjMain;//(JSONObject) jObjMain.get(i);
                JSONObject normalizedInputObj = jOfficial.getJSONObject("normalizedInput");
                JSONArray officialsArr = jOfficial.getJSONArray("officials");
                JSONArray officesArr = jOfficial.getJSONArray("offices");


                Log.d(TAG, "parseJSON: normalizedInput is = " + normalizedInputObj);

                String line1, line2, city, state, zip;
                String completeAddress = "";

                if (normalizedInputObj.has("line1")) {
                    line1 = normalizedInputObj.getString("line1");
                    completeAddress = completeAddress + line1 + " ";
                } else {
                    line1 = null;
                }
                if (normalizedInputObj.has("line2")) {
                    line2 = normalizedInputObj.getString("line2");
                    completeAddress = completeAddress + line2 + " ";
                } else {
                    line2 = null;
                }
                if (normalizedInputObj.has("city")) {
                    city = normalizedInputObj.getString("city");
                    completeAddress = completeAddress + city + " ";
                } else {
                    city = null;
                }
                if (normalizedInputObj.has("state")) {
                    state = normalizedInputObj.getString("state");
                    completeAddress = completeAddress + state + " ";
                } else {
                    state = null;
                }
                if (normalizedInputObj.has("zip")) {
                    zip = normalizedInputObj.getString("zip");
                    completeAddress = completeAddress + zip;
                } else {
                    zip = null;
                }

                Log.d(TAG, "parseJSON: completeAddress: " + completeAddress);

                officialList.add(new Official(completeAddress, officesArr, officialsArr));
            }
            //////////////////////////////////////////////////////////////////////////////////FOR LOOP
            return officialList;
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: Error Happened " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


}
