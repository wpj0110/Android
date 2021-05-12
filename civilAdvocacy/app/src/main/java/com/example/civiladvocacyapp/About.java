package com.example.civiladvocacyapp;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.civiladvocacyapp.R.layout.about;


public class About extends AppCompatActivity {
    private final static String TAG = "AboutTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        setContentView(about);
        TextView aClickable = findViewById(R.id.aboutClickable);

        super.onCreate(savedInstanceState);
        String text = "Google Civic Information API";
        SpannableString ss = new SpannableString(text);
        UnderlineSpan underlineSpan = new UnderlineSpan();
        ss.setSpan(underlineSpan,0,text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        aClickable.setText(ss);

    }
    public void onClick(View v) {
        Toast.makeText(v.getContext(), "clicked", Toast.LENGTH_SHORT).show();
        String theURL = "https://developers.google.com/civic-information/";
        //Must load to website

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(theURL));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}



