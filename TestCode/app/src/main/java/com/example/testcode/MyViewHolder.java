package com.example.testcode;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView country;
    public TextView latestDeaths;
    public TextView latestConfirmed;
    public TextView change;
    public TextView changePercentage;

    MyViewHolder(View view) {
        super(view);
        country = view.findViewById(R.id.country);
        latestDeaths = view.findViewById(R.id.latestDeaths);
        latestConfirmed = view.findViewById(R.id.latestConfirmed);
        //change = view.findViewById(R.id.change);
        //changePercentage = view.findViewById(R.id.changePercentage);
    }

}
