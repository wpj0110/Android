package com.example.civiladvocacyapp;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Picasso;

public class PhotoActivity extends AppCompatActivity {
    private final String TAG = "PhotoActivityTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.photo_layout);

        ConstraintLayout photoConstraintLayout = findViewById(R.id.photoConstraintLayout);

        ImageView dLogo2 = findViewById(R.id.dLogo2);
        ImageView rLogo2 = findViewById(R.id.rLogo2);


        TextView addressTextView3 = findViewById(R.id.addressTextView3);
        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView nameTextView = findViewById(R.id.nameTextView);

        ImageView imageView = findViewById(R.id.imageView);


        Bundle extras = getIntent().getExtras();
        String officialName = extras.getString("officialName");
        String officialParty = extras.getString("officialParty");
        String officesTitle = extras.getString("officesTitle");
        String address = extras.getString("address");
        String photoUrl = extras.getString("photoUrl");

        titleTextView.setText(officesTitle);
        nameTextView.setText(officialName);
        addressTextView3.setText(address);

        if (TextUtils.isEmpty(photoUrl) == false){
            Log.d(TAG, "onCreate: photoUrl: "+photoUrl);
            Picasso.get().load(photoUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.brokenimage)
                    .into(imageView);
        } else{
            Log.d(TAG, "onCreate: empty photoUrl: "+photoUrl);
        }


        //colors https://www.codexpedia.com/android/list-of-color-names-and-color-code-for-android/
        //color conditions https://stackoverflow.com/questions/57913995/how-to-conditionally-change-text-color-in-android
        if (officialParty.equals("Democratic Party")){
            photoConstraintLayout.setBackgroundColor(Color.parseColor("#87CEEB"));
            dLogo2.setVisibility(View.VISIBLE); //Logos
            rLogo2.setVisibility(View.GONE);
        } else if (officialParty.equals("Republican Party")){
            photoConstraintLayout.setBackgroundColor(Color.parseColor("#FF0000"));
            photoConstraintLayout.setBackgroundColor(Color.parseColor("#FF0000"));
            dLogo2.setVisibility(View.GONE); //Logos
            rLogo2.setVisibility(View.VISIBLE);
        } else{
            photoConstraintLayout.setBackgroundColor(Color.parseColor("#000000"));
            dLogo2.setVisibility(View.GONE); //Logos
            rLogo2.setVisibility(View.GONE);
        }
    }
}
