package com.example.androidnotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener{

    private static final int SECOND_ACT_REQUEST_CODE = 1;
    private final static String TAG = "MainActivity Tag";
    private static int debug_counter = 0;
    private final ArrayList<Notes> noteList = new ArrayList<>(); //notes is our object, //noteList is our array
    private final ArrayList<Notes> tempList = new ArrayList<>(); //notes is our object, //noteList is our array
    private RecyclerView recyclerView;

    private NoteListAdapter nAdapter = new NoteListAdapter(noteList,this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: "+ ++debug_counter);
        Log.d(TAG, "onCreate: array size: "+noteList.size());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);


        NoteListAdapter nAdapter = new NoteListAdapter(noteList,this);
        recyclerView.setAdapter(nAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        noteList.addAll(loadFile());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //This is for the menu to show up. Without this, it will never show.
        Log.d(TAG, "onCreateOptionsMenu: "+ ++debug_counter);
        getMenuInflater().inflate(R.menu.opt_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: "+ ++debug_counter);

        if (item.getItemId() == R.id.aboutIcon) {
            //Toast.makeText(this, "You want to do about!!!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, About.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.addIcon) {
            //Toast.makeText(this, "Going to second activity!!!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, MainActivity.class.getSimpleName());
            startActivityForResult(intent,SECOND_ACT_REQUEST_CODE);
            //noteList.add(0, new Notes("title!","descriptu"));

            //recyclerView.setAdapter(nAdapter);
            //recyclerView.setLayoutManager(new LinearLayoutManager(this));

            //nAdapter.notifyDataSetChanged();
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SECOND_ACT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult: Data Received");
                //Toast.makeText(this, "Data Received", Toast.LENGTH_SHORT).show();
                String dataTitle = data.getStringExtra("RET_TITLE");
                String dataDesc = data.getStringExtra("RET_DESC");
                String dataTime = data.getStringExtra("RET_TIME");
                Log.d(TAG, "onActivityResult: Keys successfully retrieved");

                if (dataTitle == null || dataDesc == null) {
                    //Toast.makeText(this, "Null text value returned", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dataTitle.isEmpty()) {
                    //Toast.makeText(this, "Empty Title text returned", Toast.LENGTH_SHORT).show();
                }
                if (dataDesc.isEmpty()) {
                    //Toast.makeText(this, "Empty Description text returned", Toast.LENGTH_SHORT).show();
                }

                noteList.add(0,new Notes (dataTitle,dataDesc,dataTime)); //Adding the new object to the array... I think?
                NoteListAdapter nAdapter = new NoteListAdapter(noteList,this);
                recyclerView.setAdapter(nAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));

                /*
                if (dataDesc.length() > 80) {
                    String shortenedDesc = dataDesc.substring(0,80);
                    shortenedDesc = shortenedDesc + "...";
                }
                 */
                //saveNotes();
                Log.d(TAG, "onActivityResult: User Text: " + dataTitle);
            } else {
                Log.d(TAG, "onActivityResult: result Code: " + resultCode);
            }

        } else {
            Log.d(TAG, "onActivityResult: Request Code " + requestCode);
        }
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: "+ ++debug_counter);

        Log.d(TAG, "onResume: noteList Before loading: "+noteList.size());


        Log.d(TAG, "onResume: noteList: After loading, size is "+noteList.size());

        if (noteList.size() == 0) { //If Json array is empty do nothing
            Log.d(TAG, "onResume: JSON Array is Empty "+ ++debug_counter);
            super.onResume();
            return;
        }
        saveNotes();/////////////march 20 edit

/*
        for(int i = 0; i < noteList.size(); i++) {
            Log.d(TAG, "onResume: noteList size is "+noteList.size());
            Notes n = noteList.get(i);

        }
*/
        Log.d(TAG, "onResume: Finished ");
        super.onResume();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: ");
        //saveNotes();
        super.onStop();
    }

    private ArrayList<Notes> loadFile() { //All of this section is in reference to the 2_JSONExample file.

        Log.d(TAG, "loadFile: Loading JSON File " + ++debug_counter);
        ArrayList<Notes> notList = new ArrayList<>();
        try {
            Log.d(TAG, "loadFile: after try");
            InputStream is = getApplicationContext().openFileInput("Notes.json");
            Log.d(TAG, "loadFile: after opening file");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONArray jsonArray = new JSONArray(sb.toString());
            Log.d(TAG, "loadFile: toString is done");
            ////////This section is causing me issues

            for (int i = 0; i < jsonArray.length(); i++) {
                Log.d(TAG, "loadFile: current array length: "+jsonArray.length());

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("name"); //if the "name" is not really called "name" for the json file, this will not work
                String desc = jsonObject.getString("description"); //I have "description" in my json so this should work
                String time1 = jsonObject.getString("time"); //This is not the place to get the time
                //String time1 = new Date().toString();
                Log.d(TAG, "loadFile: time is "+time1);
                //Log.d(TAG, "loadFile: time type is "+time1.);
                //time1 = "trash";
                Notes notes = new Notes(title, desc,time1);
                notList.add(notes);
                Log.d(TAG, "loadFile: added successfully, notList size is "+notList.size());
            }

            ///////////This section is causing me issues

        } catch (FileNotFoundException e) {
            //Toast.makeText(this, getString(R.string.no_file), Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(),"No file exists...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Toast.makeText(getApplicationContext(),"Loading file...", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "loadFile: returning array, notList size "+notList.size());
        return notList;
    }


    private void saveNotes() {

        Log.d(TAG, "saveNotes: Saving JSON File "+ ++debug_counter);

        try {
            FileOutputStream fos = getApplicationContext().
                    openFileOutput("Notes.json", Context.MODE_PRIVATE);

            PrintWriter printWriter = new PrintWriter(fos);
            printWriter.print(noteList);
            printWriter.close();
            fos.close();

            Log.d(TAG, "saveNotes: JSON array size "+noteList.size());
            Log.d(TAG, "saveProduct: JSON:\n" + noteList.toString());

            Toast.makeText(this, "Notes Saved!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }


    @Override
    public void onClick(View v) { //Editing the entry
        Log.d(TAG, "onClick: tapped");
        int pos = recyclerView.getChildLayoutPosition(v);
        //Notes n = noteList.get(pos);
        //Toast.makeText(v.getContext(), "SHORT " + n.toString(), Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Edit this entry?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "onClick: editing entry");

                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, MainActivity.class.getSimpleName());
                startActivityForResult(intent,SECOND_ACT_REQUEST_CODE);

                //nAdapter.notifyDataSetChanged();
                return;
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                return;
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onLongClick(View v) {
        Log.d(TAG, "onClick: long hold");
        int pos = recyclerView.getChildLayoutPosition(v);
        Notes n = noteList.get(pos);
        //Toast.makeText(v.getContext(), "LONG " + n.toString(), Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to delete this entry?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "onClick: noteList size before remove: "+noteList.size());
                noteList.remove(pos);
                Log.d(TAG, "onClick: noteList size AFTER remove: "+noteList.size());
                nAdapter.notifyDataSetChanged();
                /////////////
                recyclerView.setAdapter(nAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                saveNotes(); /////////march 20 edit
                return;
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                return;
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        return false;
    }
}