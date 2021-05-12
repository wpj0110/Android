package com.example.civiladvocacyapp;
//api key = AIzaSyC80iYlZPKSgn0JGFo2U9FSa1hs01npzfs
//sample loading of api = https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyC80iYlZPKSgn0JGFo2U9FSa1hs01npzfs&address=Chicago,IL

//Message to the TA:
//Sorry I couldn't get change update the officials after doing the address search! But everything else works fine, although
//if you really want to see it, just change the address manually using the emulator.
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener{

    private final String TAG = "MainActivityTag";

    //private List<> resultList = new ArrayList<>();
    private final List<Official> officialList = new ArrayList<>();  // Main content is here
    private RecyclerView recyclerView; // Layout's recyclerview
    private OfficialAdapter mAdapter; // Data to recyclerview adapter
    private TextView networkTextView;

    private TextView addrTextView;
    public static String locationString = "Unspecified Location";
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int LOCATION_REQUEST = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        addrTextView = findViewById(R.id.addressTextView);
        recyclerView = findViewById(R.id.recycler);
        networkTextView = findViewById(R.id.networkTextView);
        mAdapter = new OfficialAdapter(officialList,MainActivity.this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (doNetCheck() == 0) { //successful connection
            mFusedLocationClient =
                    LocationServices.getFusedLocationProviderClient(this);
            determineLocation();
        }else if (doNetCheck() == -1){
            recyclerView.setVisibility(View.GONE);
            networkTextView.setVisibility(View.VISIBLE);

        }else
            Log.d(TAG, "onCreate: bizarre network error");
    }

    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Intent intent = new Intent(MainActivity.this, OfficialActivity.class);
        String officialName, officialParty, officesTitle, address, officialAddress, officialAddressNewLine,photoUrl;
        //String channelTypeFB, channelIdFB, channelTypeTWTR, channelIdTWTR, channelTypeYT, channelIdYT;
        JSONArray officialPhone, officialURL, officialEmails, channels;
        Official o = officialList.get(pos);
        try { /////////////////JSONArray Section////////////////////
            officialEmails = o.getOfficials().getJSONObject(pos).getJSONArray("emails");
            Log.d(TAG, "onClick: email = "+officialEmails.get(0));
            intent.putExtra("officialEmail",officialEmails.get(0).toString()); //from the email array, only gets the first one.
        } catch (JSONException e) {
            Log.d(TAG, "onClick: error ");
            e.printStackTrace();
        }
        try {
            officialPhone = o.getOfficials().getJSONObject(pos).getJSONArray("phones");
            Log.d(TAG, "onClick: phone = "+officialPhone.get(0));
            intent.putExtra("officialPhone",officialPhone.get(0).toString());
        } catch (JSONException e) {
            Log.d(TAG, "onClick: error ");
            e.printStackTrace();
        }
        try {
            officialURL = o.getOfficials().getJSONObject(pos).getJSONArray("urls");
            Log.d(TAG, "onClick: url = "+officialURL.get(0));
            intent.putExtra("officialURL",officialURL.get(0).toString());
        } catch (JSONException e) {
            Log.d(TAG, "onClick: error ");
            e.printStackTrace();
        }
        try {
            //////Grabbing the Channel Section, Unfinished and I'm confused//////
            channels = o.getOfficials().getJSONObject(pos).getJSONArray("channels");
            Log.d(TAG, "onClick: channels = "+channels);
            for(int i = 0; i < channels.length(); i++){
                String tempType = channels.getJSONObject(i).getString("type");
                String tempID = channels.getJSONObject(i).getString("id");

                switch(tempType){
                    case "Twitter" :
                        intent.putExtra("channelTwitter", tempType);
                        intent.putExtra("channelTwitterID", tempID);
                        Log.d(TAG, "onClick: channelName = "+tempType);
                        Log.d(TAG, "onClick: channelID = "+tempID);
                        Log.d(TAG, "onClick: ////////////////////////////////");
                        break;
                    case "Facebook":
                        intent.putExtra("channelFacebook", tempType);
                        intent.putExtra("channelFacebookID", tempID);
                        Log.d(TAG, "onClick: channelName = "+tempType);
                        Log.d(TAG, "onClick: channelID = "+tempID);
                        Log.d(TAG, "onClick: ////////////////////////////////");
                        break;
                    case "YouTube":
                        intent.putExtra("channelYouTube", tempType);
                        intent.putExtra("channelYouTubeID", tempID);
                        Log.d(TAG, "onClick: channelName = "+tempType);
                        Log.d(TAG, "onClick: channelID = "+tempID);
                        Log.d(TAG, "onClick: ////////////////////////////////");
                        break;
                    default :
                        Log.d(TAG, "onClick: no channel found");
                        Log.d(TAG, "onClick: ////////////////////////////////");
                }
            }
            ////////////////////////////////////////////////////////////////////
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        try {
            officialName = o.getOfficials().getJSONObject(pos).get("name").toString(); //Name of the Official
            intent.putExtra("officialName", officialName);
            Log.d(TAG, "onClick: officialName "+officialName);
        } catch (JSONException e) {
            Log.d(TAG, "onClick: error ");
            e.printStackTrace();
        }
        try {
            officialParty = o.getOfficials().getJSONObject(pos).get("party").toString(); //Party of belonging
            intent.putExtra("officialParty", officialParty);
            Log.d(TAG, "onClick: officialParty "+officialParty);
        } catch (JSONException e) {
            Log.d(TAG, "onClick: error ");
            e.printStackTrace();
        }
        try {
            officesTitle = o.getOffices().getJSONObject(pos).get("name").toString(); //Title
            intent.putExtra("officesTitle", officesTitle);
            Log.d(TAG, "onClick: officesTitle "+officesTitle);
        } catch (JSONException e) {
            Log.d(TAG, "onClick: error ");
            e.printStackTrace();
        }

        try {
            photoUrl = o.getOfficials().getJSONObject(pos).get("photoUrl").toString();
            intent.putExtra("photoUrl", photoUrl);
            Log.d(TAG, "onClick: photoUrl "+photoUrl);
        } catch (JSONException e) {
            Log.d(TAG, "onClick: error ");
            e.printStackTrace();
        }
        try {
            //The Official's address
            officialAddressNewLine = dealingWithAddressNewLine(o.getOfficials().getJSONObject(pos).getJSONArray("address")); //
            intent.putExtra("officialAddressNewLine", officialAddressNewLine);
            Log.d(TAG, "onClick: officialAddressNewLine "+officialAddressNewLine);
            officialAddress = dealingWithAddress(o.getOfficials().getJSONObject(pos).getJSONArray("address")); //
            intent.putExtra("officialAddress", officialAddress);
            Log.d(TAG, "onClick: officialAddress"+officialAddress);
        } catch (JSONException e) {
            Log.d(TAG, "onClick: error ");
            e.printStackTrace();
        }
        address = o.getAddress(); //current address chosen
        intent.putExtra("address", address);

        //https://stackoverflow.com/questions/5265913/how-to-use-putextra-and-getextra-for-string-data
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.opt_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.searchMenu) {
            Log.d(TAG, "onOptionsItemSelected: add");

            if (doNetCheck() != 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);


                builder.setMessage("Addresses Cannot Be Searched Without A Network Connection");
                builder.setTitle("No Network Connection");

                AlertDialog dialog = builder.create();
                dialog.show();
                return super.onOptionsItemSelected(item);
            }

            Toast.makeText(MainActivity.this, "search", Toast.LENGTH_SHORT).show();
            // Single input value dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final EditText et = new EditText(this);
            et.setInputType(InputType.TYPE_CLASS_TEXT);
            et.setGravity(Gravity.CENTER_HORIZONTAL);
            builder.setView(et);
            builder.setPositiveButton("SEARCH", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Log.d(TAG, "onClick: searching");
                    ArrayList<String> addressResults = new ArrayList<>();
                    String searchEntry = et.getText().toString();
                    addressResults = doLocationName(searchEntry);

                    //Log.d(TAG, "onOptionsItemSelected: search is " +searchEntry);
                    Toast.makeText(MainActivity.this, "Sorry this doesn't work! Try changing the location instead manually", Toast.LENGTH_LONG).show();
                    Toast.makeText(MainActivity.this, "Sorry this doesn't work! Try changing the location instead manually", Toast.LENGTH_LONG).show();
                    if (addressResults.size() > 0){
                        Log.d(TAG, "onClick: showing search results");
                        showResults(addressResults);
                    } else{
                        Log.d(TAG, "onClick: no results");
                        Toast.makeText(MainActivity.this, "No Addresses Found!", Toast.LENGTH_SHORT).show();
                    }


                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Log.d(TAG, "onClick: cancelled search");
                    Toast.makeText(MainActivity.this, "You changed your mind!", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setMessage("Please Enter Address:");
            builder.setTitle("Search:");

            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }else if (item.getItemId() == R.id.aboutMenu){
            Toast.makeText(MainActivity.this, "about", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, About.class);
            startActivity(intent);
            return true;
        }else{
            Log.d(TAG, "onOptionsItemSelected: nothing");
        }
        return super.onOptionsItemSelected(item);
    }

    public void downloadFailed() {
        Log.d(TAG, "downloadFailed: ");
        officialList.clear();
        mAdapter.notifyDataSetChanged();
    }

    public void updateData(ArrayList<Official> oList) {
        Log.d(TAG, "updateData: FIX THIS!!!");

        for (int i = 0; i < oList.size(); i++){
            Log.d(TAG, "updateData: oList size = "+oList.size()+" index "+i+" Value: "+oList.get(0));
        }

        officialList.addAll(oList);
        String address = officialList.get(0).getAddress();
        addrTextView.setText(address);
        mAdapter.notifyDataSetChanged();
    }

    ///////////LOADING ADDRESS////////////////////////////////////////
    private void determineLocation() {
        Log.d(TAG, "determineLocation: ");
        if (checkPermission()) {
            Log.d(TAG, "determineLocation: yes permission ");
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            locationString = String.format(Locale.getDefault(),
                                    "%.5f, %.5f", location.getLatitude(), location.getLongitude());
                            Toast.makeText(MainActivity.this, "Location: "+locationString, Toast.LENGTH_SHORT).show();
                            //urlLoader(locationString);
                            OffLoaderRunnable offLoaderRunnable = new OffLoaderRunnable(this,locationString);// Load the data
                            new Thread(offLoaderRunnable).start();// Load the data
                            //addrTextView.setText(locationString);
                        }
                    })
                    .addOnFailureListener(this, e -> Toast.makeText(MainActivity.this,
                            e.getMessage(), Toast.LENGTH_LONG).show());
        }
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, LOCATION_REQUEST);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    determineLocation();
                } else {
                    addrTextView.setText(R.string.troubleLocation);
                }
            }
        }
    }

    private String dealingWithAddress(JSONArray jArrMain) throws JSONException {
        JSONArray jOfficial = jArrMain;
        JSONObject normalizedInputObj = (JSONObject) jOfficial.get(0);

        Log.d(TAG, "dealingWithAddress: ");

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

        return completeAddress;
    }

    private String dealingWithAddressNewLine(JSONArray jArrMain) throws JSONException {
        JSONArray jOfficial = jArrMain;
        JSONObject normalizedInputObj = (JSONObject) jOfficial.get(0);

        Log.d(TAG, "dealingWithAddress: ");

        String line1, line2, city, state, zip;
        String completeAddress = "";

        if (normalizedInputObj.has("line1")) {
            line1 = normalizedInputObj.getString("line1");
            completeAddress = completeAddress + line1 + " \n";
        } else {
            line1 = null;
        }
        if (normalizedInputObj.has("line2")) {
            line2 = normalizedInputObj.getString("line2");
            completeAddress = completeAddress + line2 + " ";
        } else {
            Log.d(TAG, "dealingWithAddressNewLine: line2 = null");
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

        return completeAddress;
    }

    //////////////////////////////////////////////////////////////////////////////////////

    public ArrayList<String> doLocationName(String addressInput) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        ArrayList<String> tempList = new ArrayList<>();
        try {
            List<Address> addresses;

            String loc = addressInput;
            addresses = geocoder.getFromLocationName(loc, 10);
            //for(int i = 0 ; i < addresses.size(); i++){
            //    Log.d(TAG, "doLocationName: addresses = "+addresses.get(i));
            //}
            tempList = displayAddresses(addresses);

            //return tempList;
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return tempList;
    }

    private ArrayList<String> displayAddresses(List<Address> addresses) {
        StringBuilder sb = new StringBuilder();
        ArrayList<String> tempList = new ArrayList<>(addresses.size());
        if (addresses.size() == 0) {
            Log.d(TAG, "displayAddresses: nothing found");
            return null;
        }

        for (Address ad : addresses) {

            String a = String.format("%s %s %s %s %s %s",
                    (ad.getSubThoroughfare() == null ? "" : ad.getSubThoroughfare()),
                    (ad.getThoroughfare() == null ? "" : ad.getThoroughfare()),
                    (ad.getLocality() == null ? "" : ad.getLocality()),
                    (ad.getAdminArea() == null ? "" : ad.getAdminArea()),
                    (ad.getPostalCode() == null ? "" : ad.getPostalCode()),
                    (ad.getCountryName() == null ? "" : ad.getCountryName()));

            if (!a.trim().isEmpty())
                sb.append("* ").append(a.trim());

            //sb.append("\n");
            //Log.d(TAG, "displayAddresses: address sb= "+sb.toString());
            Log.d(TAG, "displayAddresses: address String a = "+a);
            //tempList.add(sb.toString());
            tempList.add(a);
        }
        //((TextView) findViewById(R.id.textView)).setText(sb.toString());
        return tempList;
    }

    public void showResults(ArrayList<String> searchResults){
        // List selection dialog

        //ake an array of strings
        int listSize = searchResults.size();
        //final int[] chosenIndex = new int[1];
        final CharSequence[] sArray = new CharSequence[listSize];
        for (int i = 0; i < listSize; i++)
            sArray[i] = searchResults.get(i);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Make a selection");

        // Set the builder to display the string array as a selectable
        // list, and add the "onClick" for when a selection is made
        builder.setItems(sArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Chosen something! "+searchResults.get(which), Toast.LENGTH_SHORT).show();
                OffLoaderRunnable offLoaderRunnable = new OffLoaderRunnable(MainActivity.this,searchResults.get(which));// Load the data
                new Thread(offLoaderRunnable).start();// Load the data
                mAdapter.notifyDataSetChanged();

            }
        });

        builder.setNegativeButton("Nevermind", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Toast.makeText(MainActivity.this, "You changed your mind!", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();

        dialog.show();

    }
    private int doNetCheck() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            Toast.makeText(this, "Cannot access ConnectivityManager", Toast.LENGTH_SHORT).show(); //Connection failure
            return -1;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo(); // don't forget  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>

        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected){ //Connection Success
            return 0;
        } else{ //Connection failure
            return -1;
        }
    }
}