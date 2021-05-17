package com.example.testcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
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
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity
implements View.OnClickListener, View.OnLongClickListener {

    private final static String TAG = "MainActivityTAG";

    private boolean isConnected;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayAdapter<String> arrayAdapter;
    private DatabaseHandler databaseHandler;

    private final List<Covid> covidList = new ArrayList<>(); //main list
    private ArrayList<Country> countryData = new ArrayList<>();
    private final ArrayList<String> countryStringList = new ArrayList<>();
    private final ArrayList<ArrayList<String>> covidListDB = new ArrayList<>();

    static String country;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //DrawerList guide https://www.geeksforgeeks.org/navigation-drawer-in-android/
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.drawer_list);

        // Set up the drawer item click callback method
        mDrawerList.setOnItemClickListener(
                (parent, view, position, id) -> {
                    selectItem(position);
                    mDrawerLayout.closeDrawer(mDrawerList);
                }
        );

        // Create the drawer toggle
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        );

        //needed this to show the drawerList on the action bar.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }


        // Load all the countries
        if (countryData.isEmpty()) {
            CountryLoaderRunnable clr = new CountryLoaderRunnable(this);
            new Thread(clr).start();
        }


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) { //nothing to change
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) { //nothing to change
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    private void selectItem(int position) {

        mDrawerLayout.closeDrawer(mDrawerList);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            Log.d(TAG, "onOptionsItemSelected: mDrawerToggle " + item);
            return true;
        }

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

        Log.d(TAG, "updateCountry: listIn = "+listIn);
        for (int i = 0 ; i < listIn.size() ; i++)
            countryStringList.add(listIn.get(i).toString());
        countryData = listIn;
        arrayAdapter = new ArrayAdapter<>(this, R.layout.drawer_item, countryStringList);
        mDrawerList.setAdapter(arrayAdapter);

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

    public void loadCountries(){ //using the runnable, and also the database
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) { //loads all country names first from the api
            CountryLoaderRunnable countryLoaderRunnable = new CountryLoaderRunnable(this);
            new Thread(countryLoaderRunnable).start();
        }

        databaseHandler.dumpDbToLog();

        ArrayList<ArrayList<String>> list = databaseHandler.loadCovid();
        covidListDB.clear();
        covidListDB.addAll(list);

        covidList.clear(); //ensures the list is really empty

        for (int i = 0; i< covidListDB.size(); i++){
            if (isConnected) {
                CovidRunnable covidLoaderRunnable = new CovidRunnable(MainActivity.this, covidListDB.get(i).get(0));
                new Thread(covidLoaderRunnable).start();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else{
                String confirmed = covidListDB.get(i).get(0);
                String country = covidListDB.get(i).get(1);
                String recovered = covidListDB.get(i).get(2);
                String critical = covidListDB.get(i).get(3);
                String deaths = covidListDB.get(i).get(4);
                String lastChange = covidListDB.get(i).get(5);
                String lastUpdate = covidListDB.get(i).get(6);

                Covid countryCovid = new Covid(confirmed, country, recovered, critical, deaths,lastChange,lastUpdate);
                addCountry(countryCovid);
            }
        }

        if (!isConnected){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("No Network Connection");
            builder.setMessage("Stocks cannot be updated without a network connection");
            AlertDialog dialog1 = builder.create();
            dialog1.show();
        }
    }

    public void addCountry(Covid countryCovid) {
        databaseHandler.addCountry(countryCovid);
        covidList.add(countryCovid);

        ArrayList<ArrayList<String>> list = databaseHandler.loadCovid();
        covidListDB.clear();
        covidListDB.addAll(list);

        //sort list
        ArrayList<String> DBTemp = new ArrayList<>();
        ArrayList<Covid> tempCovidList = new ArrayList<>(covidList);

        for (int i=0;i<covidListDB.size();i++){
            DBTemp.add(covidListDB.get(i).get(0));
        }

        Collections.sort(DBTemp);
        Log.d(TAG, "addCovid: DBTemp size:" + DBTemp.size());
        Log.d(TAG, "addCovid: tempCovidList size:" + tempCovidList.size());
        covidList.clear();

        for (int i=0;i<DBTemp.size();i++){
            for (int j=0;j<tempCovidList.size();j++){
                if (tempCovidList.get(j).getCountry().equals(DBTemp.get(i))) {
                    covidList.add(tempCovidList.get(j));
                }
            }
        }

        sAdapter.notifyDataSetChanged();
    }

    public void removeStock(int index) {
        if (!covidList.isEmpty()) {
            databaseHandler.deleteCountry(covidList.get(index).getCountry());
            covidList.remove(index);
            sAdapter.notifyDataSetChanged();
        }
    }

}