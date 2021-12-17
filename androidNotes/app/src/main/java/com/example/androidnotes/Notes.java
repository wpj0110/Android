package com.example.androidnotes;

import android.util.JsonWriter;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;

public class Notes {

    private final String title;
    private final String desc;
    private String time;
    private final static String TAG = "NotesTag";


    public Notes(String title, String desc, String time) {
        this.title = title;
        this.desc = desc;
        this.time = time;;
    }

    public String getTitle() {
        return title;
    }

    String getDesc() {
        return desc;
    }

    String getTime() {
        //time = new Date().toString();
        return time;
    }

    @NonNull
    public String toString() { //This is in reference to the 2_JSONExample file.
        Log.d(TAG, "toString: ");

        try {
            StringWriter sw = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(sw);
            jsonWriter.setIndent("  ");
            jsonWriter.beginObject();
            jsonWriter.name("name").value(getTitle());
            jsonWriter.name("description").value(getDesc());
            jsonWriter.name("time").value(getTime());
            jsonWriter.endObject();
            jsonWriter.close();
            return sw.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
