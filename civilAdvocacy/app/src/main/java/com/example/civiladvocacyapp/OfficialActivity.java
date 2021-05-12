package com.example.civiladvocacyapp;

//https://stackoverflow.com/questions/5265913/how-to-use-putextra-and-getextra-for-string-data

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Picasso;

public class OfficialActivity extends AppCompatActivity
        implements View.OnClickListener{
    private final String TAG = "OfficialActivityTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");

        setContentView(R.layout.officials_layout);

        Bundle extras = getIntent().getExtras();
        String officialName = extras.getString("officialName");
        String officialParty = extras.getString("officialParty");
        String officesTitle = extras.getString("officesTitle");
        String officialAddress = extras.getString("officialAddress"); //For the Clickable?
        String officialAddressNewLine = extras.getString("officialAddressNewLine");
        String address = extras.getString("address");
        String officialPhone = extras.getString("officialPhone"); //Must be clickable
        String officialEmail = extras.getString("officialEmail"); //Must be clickable
        String officialURL = extras.getString("officialURL"); //Must be clickable
        String photoUrl = extras.getString("photoUrl");

        String facebookChannel = extras.getString("channelFacebook");
        String facebookID = extras.getString("channelFacebookID");
        String twitterChannel = extras.getString("channelTwitter");
        String twitterID = extras.getString("channelTwitterID");
        String youtubeChannel = extras.getString("channelYouTube");
        String youtubeID = extras.getString("channelYouTubeID");


        //officialEmail = "blah@yahoo.com"; //debugging purposes

        //Adding the Underline
        UnderlineSpan underlineSpan = new UnderlineSpan();

        //TextViews and their id's
        TextView tLayoutTextView = findViewById(R.id.tLayoutTextView);
        TextView nLayoutTextView = findViewById(R.id.nLayoutTextView);
        TextView pLayoutTextView = findViewById(R.id.pLayoutTextView);
        TextView aLayoutTextView = findViewById(R.id.addressTextView2);
        TextView offAddressTextView= findViewById(R.id.setAddressTV);
        TextView phoneLayoutTextView = findViewById(R.id.setPhoneTV);
        TextView emailLayoutTextView = findViewById(R.id.setEmailTV);
        TextView urlLayoutTextView = findViewById(R.id.setWebsiteTV);

        TextView emailAligned = findViewById(R.id.emailAligned);
        TextView urlAligned = findViewById(R.id.urlAligned);
        TextView phoneAligned = findViewById(R.id.phoneAligned);
        TextView addressAligned = findViewById(R.id.addressAligned);

        ConstraintLayout colorLayout = findViewById(R.id.colorLayout); //change these colors
        ScrollView scrollView = findViewById(R.id.scrollView); //change these colors

        ImageView dLogo = findViewById(R.id.dLogo);
        ImageView rLogo = findViewById(R.id.rLogo);

        ImageView facebookLogo = findViewById(R.id.facebookLogo);
        ImageView twitterLogo = findViewById(R.id.twitterLogo);
        ImageView youtubeLogo = findViewById(R.id.youtubeLogo);

        ImageView placeHolderImageView = findViewById(R.id.placeHolderImageView);
        ImageView defaultImageView = findViewById(R.id.defaultImageView);
        //ImageView badImageView = findViewById(R.id.badImageView);

        //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(placeHolderImageView); //this is working!
        if (TextUtils.isEmpty(photoUrl) == false){
            Log.d(TAG, "onCreate: photoUrl: "+photoUrl);
            Picasso.get().load(photoUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.brokenimage)
                    .into(placeHolderImageView);
            placeHolderImageView.setVisibility(View.VISIBLE);
            defaultImageView.setVisibility(View.INVISIBLE);
        } else{
            Log.d(TAG, "onCreate: empty photoUrl: "+photoUrl);
            placeHolderImageView.setVisibility(View.GONE);
        }




        //Official details (Website, Address, Email, Phone)
        if (TextUtils.isEmpty(officialURL) == false){
            SpannableString ssOfficialURL = new SpannableString(officialURL);
            ssOfficialURL.setSpan(underlineSpan,0,officialURL.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            urlLayoutTextView.setText(ssOfficialURL);
        } else{
            //make the textview gone
            urlLayoutTextView.setVisibility(View.GONE);
            urlAligned.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(officialEmail) == false) {
            SpannableString ssOfficialEmail = new SpannableString(officialEmail);
            ssOfficialEmail.setSpan(underlineSpan,0,officialEmail.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            emailLayoutTextView.setText(ssOfficialEmail);
        } else{
            //make the textview gone
            emailLayoutTextView.setVisibility(View.GONE);
            emailAligned.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(officialAddressNewLine) == false) {
            SpannableString ssOfficialAddressNewLine = new SpannableString(officialAddressNewLine);
            ssOfficialAddressNewLine.setSpan(underlineSpan,0,officialAddressNewLine.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            offAddressTextView.setText(ssOfficialAddressNewLine);
        } else{
            //make the textview gone
            aLayoutTextView.setVisibility(View.GONE);
            addressAligned.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(officialPhone) == false) {
            SpannableString ssOfficialPhone = new SpannableString(officialPhone);
            ssOfficialPhone.setSpan(underlineSpan,0,officialPhone.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            phoneLayoutTextView.setText(ssOfficialPhone);
        } else{
            //make the textview gone
            pLayoutTextView.setVisibility(View.GONE);
            phoneAligned.setVisibility(View.GONE);
        }

        //officialParty = "blah"; //test if unknown party
        //officialParty = "Republican Party"; //test if republican
        //if-else if party is neither Democrat or Republican
        if (((officialParty.equals("Democratic Party")) || !(officialParty.equals("Republican Party"))) == false){
            Log.d(TAG, "onCreate: officialParty is unknown");
            pLayoutTextView.setText("Unknown Party");
        } else{ //otherwise, everything is normal
            pLayoutTextView.setText(officialParty);
        }

        tLayoutTextView.setText(officesTitle);
        nLayoutTextView.setText(officialName);
        aLayoutTextView.setText(address);


        //colors https://www.codexpedia.com/android/list-of-color-names-and-color-code-for-android/
        //color conditions https://stackoverflow.com/questions/57913995/how-to-conditionally-change-text-color-in-android
        if (officialParty.equals("Democratic Party")){
            colorLayout.setBackgroundColor(Color.parseColor("#87CEEB"));
            scrollView.setBackgroundColor(Color.parseColor("#87CEEB"));
            dLogo.setVisibility(View.VISIBLE); //Logos
            rLogo.setVisibility(View.GONE);
        } else if (officialParty.equals("Republican Party")){
            colorLayout.setBackgroundColor(Color.parseColor("#FF0000"));
            scrollView.setBackgroundColor(Color.parseColor("#FF0000"));
            dLogo.setVisibility(View.GONE); //Logos
            rLogo.setVisibility(View.VISIBLE);
        } else{
            colorLayout.setBackgroundColor(Color.parseColor("#000000"));
            scrollView.setBackgroundColor(Color.parseColor("#000000"));
            dLogo.setVisibility(View.GONE); //Logos
            rLogo.setVisibility(View.GONE);
        }

        //facebook, twitter, youtube logos
        if (TextUtils.isEmpty(facebookChannel) == false) {
            facebookLogo.setVisibility(View.VISIBLE);
        }else{
            facebookLogo.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(twitterChannel) == false){
            twitterLogo.setVisibility(View.VISIBLE);
        }else{
            twitterLogo.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(youtubeChannel) == false) {
            youtubeLogo.setVisibility(View.VISIBLE);
        }else{
            youtubeLogo.setVisibility(View.GONE);
        }


    }
    //Website onClick
    public void onClickURL(View v) {
        Log.d(TAG, "onClickURL: ");
        Bundle extras = getIntent().getExtras();
        String officialURL = extras.getString("officialURL"); //Must be clickable
        //Must load to website

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(officialURL));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    //https://stackoverflow.com/questions/2662531/launching-google-maps-directions-via-an-intent-on-android
    public void onClickDirections(View v){
        Log.d(TAG, "onClickDirections: ");

        Bundle extras = getIntent().getExtras();
        String officialAddress = extras.getString("officialAddress");

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr="+officialAddress));
        startActivity(intent);
    }

    //https://stackoverflow.com/questions/34596644/android-intent-call-number
    public void onClickDial(View v){
        Log.d(TAG, "onClickDial: ");
        Bundle extras = getIntent().getExtras();
        String officialPhone = extras.getString("officialPhone");
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+officialPhone));
        startActivity(intent);
    }
    //https://stackoverflow.com/questions/8701634/send-email-intent
    public void onClickEmail(View v){
        Log.d(TAG, "onClickEmail: ");
        Bundle extras = getIntent().getExtras();
        String officialEmail = extras.getString("officialEmail"); //Must be clickable

        officialEmail = "blah@yahoo.com"; //debugging purposes

        String[] temp = new String[1];
        temp[0] = officialEmail;

        Uri uri = Uri.parse("mailto:" + officialEmail)
                .buildUpon()
                .build();

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
        startActivity(Intent.createChooser(emailIntent,""));
    }

    public void twitterClicked(View v) {
        Intent intent = null;
        Bundle extras = getIntent().getExtras();
        String twitterChannel = extras.getString("channelTwitter");
        String twitterID = extras.getString("channelTwitterID");
        String name = twitterID;
        try {
            // get the Twitter app if possible
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
        }
        startActivity(intent);
    }




public void facebookClicked(View v) {
    Bundle extras = getIntent().getExtras();
    String facebookChannel = extras.getString("channelFacebook");
    String facebookID = extras.getString("channelFacebookID");
    String FACEBOOK_URL = "https://www.facebook.com/" + facebookID;
    String urlToUse;
    PackageManager packageManager = getPackageManager();
    try {
        int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
        if (versionCode >= 3002850) { //newer versions of fb app
            urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
        } else { //older versions of fb app
            //urlToUse = "fb://page/" + channels.get("Facebook");
            urlToUse = "fb://page/" + facebookChannel;
        }
    } catch (PackageManager.NameNotFoundException e) {
        urlToUse = FACEBOOK_URL; //normal web url
    }
    Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
    facebookIntent.setData(Uri.parse(urlToUse));
    startActivity(facebookIntent);
}


public void youTubeClicked(View v) {
    Bundle extras = getIntent().getExtras();
    String youtubeChannel = extras.getString("channelYouTube");
    String youtubeID = extras.getString("channelYouTubeID");
    String name = youtubeID;
    Intent intent = null;
    try {
        intent = new Intent(Intent.ACTION_VIEW);
        intent.setPackage("com.google.android.youtube");
        intent.setData(Uri.parse("https://www.youtube.com/" + name));
        startActivity(intent);
    } catch (ActivityNotFoundException e) {
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/" + name)));
    }
}

public void onClickPhoto(View v){
    Bundle extras = getIntent().getExtras();
    Intent intent = new Intent(OfficialActivity.this, PhotoActivity.class);

    String officialName = extras.getString("officialName");
    String officialParty = extras.getString("officialParty");
    String officesTitle = extras.getString("officesTitle");
    String address = extras.getString("address");
    String photoUrl = extras.getString("photoUrl");

    intent.putExtra("officialName", officialName);
    intent.putExtra("officialParty", officialParty);
    intent.putExtra("officesTitle", officesTitle);
    intent.putExtra("address", address);
    intent.putExtra("photoUrl", photoUrl);

    startActivity(intent);
}


    @Override
    public void onClick(View v) {

    }
}
