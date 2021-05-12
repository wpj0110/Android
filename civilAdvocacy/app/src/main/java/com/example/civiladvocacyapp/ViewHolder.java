package com.example.civiladvocacyapp;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView titleTextView;
    TextView nameTextView;
    TextView partyTextView;

    ViewHolder(View view) {
        super(view);
        titleTextView = view.findViewById(R.id.viewHolderTitle);
        nameTextView = view.findViewById(R.id.viewHolderName);
        partyTextView = view.findViewById(R.id.viewHolderParty);
    }

}
