package com.example.testcode;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
implements View.OnClickListener, View.OnLongClickListener {

    private final static String TAG = "MainActivityTAG";

    private boolean isConnected;

    private ArrayList<Country> countryData = new ArrayList<>();
    private final ArrayList<ArrayList<String>> stockListDB = new ArrayList<>();

    static String country;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load all the countries
        if (countryData.isEmpty()) {
            CountryLoaderRunnable clr = new CountryLoaderRunnable(this);
            new Thread(clr).start();
        }



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.add) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

            if (!isConnected){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("No Network Connection");
                builder.setMessage("Countries cannot be added without a network connection");
                AlertDialog dialog1 = builder.create();
                dialog1.show();
                return false;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            final EditText et = new EditText(this);
            et.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
            et.setInputType(InputType.TYPE_CLASS_TEXT);
            et.setGravity(Gravity.CENTER_HORIZONTAL);
            builder.setView(et);

            HashMap<String, String> matching = new HashMap<>();

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    String input;
                    input = et.getText().toString();
                    country = input;
////////////////////////////////////////////
                    Log.d(TAG, "onClick: CountryData is "+countryData);

                    ArrayList<String> tempList = new ArrayList<>();
                    for (int i = 0 ; i < countryData.size() ; i++){
                        if(countryData.get(i).toString().contains(input)){
                            tempList.add(countryData.get(i).toString());
                        } else{
                            //Log.d(TAG, "onClick: setgetvalue indexed "+set.getValue().get(0));
                        }
                    }
                    Log.d(TAG, "onClick: tempList is "+tempList);

                    if (tempList.size() == 0){
                        Log.d(TAG, "onClick: no matches");
                        noMatches();
                    }
                    else if (tempList.size() == 1){
                        Log.d(TAG, "onClick: one match");
                        oneMatch(country);
                    }
                    else if (tempList.size() > 1){
                        Log.d(TAG, "onClick: many matches: " + tempList.size());
                        manyMatches(tempList);
                    }
                    else {
                        Log.d(TAG, "onClick: weird result (negative tempList size)");
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id){}//do nothing
            });

            builder.setTitle("Country");
            builder.setMessage("Please enter the country");

            AlertDialog dialog = builder.create();
            dialog.show();

            return true;
        }

        return false;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    public void updateCountry(ArrayList<Country> listIn) {

        Log.d(TAG, "updateCountry: listln = "+listIn);
        countryData = listIn;

    }

    public void noMatches(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("No matches");
        builder.setMessage("Try a different query, or Look it up from the list.");
        AlertDialog dialog1 = builder.create();
        dialog1.show();
    }

    public void oneMatch(String countryPassed) {
        CovidRunnable covidRunnable = new CovidRunnable(MainActivity.this, countryPassed);
        new Thread(covidRunnable).start();
    }

    public void manyMatches(ArrayList<String> passedList){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Make a selection");
        //make an array of strings
        final CharSequence[] sArray = new CharSequence[passedList.size()];
        for (int i = 0; i < passedList.size(); i++)
            sArray[i] = passedList.get(i);

        builder.setItems(sArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(MainActivity.this, "Chosen something! "+which, Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "Chosen something! "+sArray[which], Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Nevermind", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(MainActivity.this, "You changed your mind!", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();

        dialog.show();

    }

}