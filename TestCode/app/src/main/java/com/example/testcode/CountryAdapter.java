package com.example.testcode;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class CountryAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private final List<Covid> covidList;
    private final MainActivity mainAct;

    CountryAdapter(List<Covid> covidList, MainActivity ma) {
        this.covidList = covidList;
        mainAct = ma;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.country_row, parent, false);

        itemView.setOnClickListener((View.OnClickListener) mainAct);
        itemView.setOnLongClickListener((View.OnLongClickListener) mainAct);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Covid countryCovid = covidList.get(position);

        holder.country.setText(countryCovid.getCountry());
        holder.latestDeaths.setText(countryCovid.getDeaths());
        holder.latestConfirmed.setText(countryCovid.getConfirmed());
        //holder.latestConfirmed.setText(String.format(Locale.getDefault(), "%.2f", covidList.getLatestPrice()));
        //holder.change.setText(String.format(Locale.getDefault(), "%.2f", stock.getChange()));
        //String temp = String.format(Locale.getDefault(), "(%.2f", stock.getChangePercentage()) + "%)";
        //holder.changePercentage.setText(temp);

        /*
        if (stock.getChange() < 0.0){
            holder.country.setTextColor(Color.RED);
            holder.latestDeaths.setTextColor(Color.RED);
            holder.latestConfirmed.setTextColor(Color.RED);
            holder.change.setTextColor(Color.RED);
            holder.changePercentage.setTextColor(Color.RED);

            String temp1 = "▼ " + holder.change.getText().toString();
            holder.change.setText(temp1);
        }
        else{
            holder.country.setTextColor(Color.GREEN);
            holder.latestDeaths.setTextColor(Color.GREEN);
            holder.latestConfirmed.setTextColor(Color.GREEN);
            holder.change.setTextColor(Color.GREEN);
            holder.changePercentage.setTextColor(Color.GREEN);

            String temp1 = "▲ " + holder.change.getText().toString();
            holder.change.setText(temp1);
        }

         */
    }

    @Override
    public int getItemCount() {
        return covidList.size();
    }
}
