package com.example.androidnotes;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class NoteViewHolder extends RecyclerView.ViewHolder {

    TextView title;
    TextView desc;
    TextView dateTime;

    public NoteViewHolder(@NonNull View itemview) {
        super(itemview);

        title = itemview.findViewById(R.id.noteTitle);
        desc = itemview.findViewById(R.id.noteDesc);
        dateTime = itemview.findViewById(R.id.timeStamp); //This is for the time
    }

}
