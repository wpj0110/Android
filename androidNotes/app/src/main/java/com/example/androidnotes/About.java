package com.example.androidnotes;



import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import static com.example.androidnotes.R.layout.about;
//import static com.example.androidnotes.R.layout.activity_second;

public class About extends AppCompatActivity {
    private final static String TAG = "AboutTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(about);
        //setContentView(activity_second);
    }
}



