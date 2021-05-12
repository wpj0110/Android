package com.example.civiladvocacyapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class OfficialAdapter extends RecyclerView.Adapter<ViewHolder>{

    private static final String TAG = "OfficialAdapterTAG";
    private final List<Official> officialList;
    //private final List<OfficialRecycler> officialRecyclerList;
    private final MainActivity mainAct;

    OfficialAdapter(List<Official> offList, MainActivity ma) {
    //OfficialAdapter(List<OfficialRecycler> offList, MainActivity ma) {
        this.officialList = offList;
        //this.officialRecyclerList = offList;
        mainAct = ma;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        Official official = officialList.get(position);
        String aArr = officialList.get(position).getAddress();
        JSONArray oArr = officialList.get(position).getOffices();
        JSONArray nArr = officialList.get(position).getOfficials();
        try {
            holder.titleTextView.setText(oArr.getJSONObject(position).get("name").toString());
            holder.nameTextView.setText(nArr.getJSONObject(position).get("name").toString());
            holder.partyTextView.setText(nArr.getJSONObject(position).get("party").toString());
        } catch (JSONException e) {
            Log.d(TAG, "onBindViewHolder: Error");
            e.printStackTrace();
        }
        //officialRecyclerList.get(position);
        //holder.titleTextView.setText(officialRecyclerList.get(position).getTitle());
        //holder.nameTextView.setText(officialRecyclerList.get(position).getName());
        //holder.partyTextView.setText(officialRecyclerList.get(position).getParty());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW MyViewHolder");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.official_list_row, parent, false);

        itemView.setOnClickListener(mainAct);

        return new ViewHolder(itemView);
    }
    @Override
    public int getItemCount() {
        return officialList.size();
        //return officialRecyclerList.size();
    }

}
