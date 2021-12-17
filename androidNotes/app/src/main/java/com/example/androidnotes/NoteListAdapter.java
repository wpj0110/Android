package com.example.androidnotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

public class NoteListAdapter extends RecyclerView.Adapter<NoteViewHolder>{

    private ArrayList<Notes> nList;
    private final MainActivity mainAct;

    public NoteListAdapter(ArrayList<Notes> nList, MainActivity ma){
        this.nList = nList;
        mainAct = ma;
    }
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_entry, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Notes n = nList.get(position);
        holder.desc.setText(n.getDesc());
        holder.title.setText(n.getTitle());
        holder.dateTime.setText(n.getTime());
        //holder.dateTime.setText("Time Here, Temporary Text");
    }

    @Override
    public int getItemCount() {
        return nList.size();
    }
}
