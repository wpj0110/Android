package com.example.androidnotes;

import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SecondActivity extends AppCompatActivity {

    private Date currentTime = Calendar.getInstance().getTime();

    private final static String TAG = "SecondActivity Tag";

    private EditText editText1;
    private EditText editText2;
    private TextView dateText;
    private TextView hiddenTextView;

    private final ArrayList<Notes> noteList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        editText1 = findViewById(R.id.editTitle);
        editText2 = findViewById(R.id.editDesc);
        dateText = findViewById(R.id.timeStamp);
        hiddenTextView = findViewById(R.id.hiddenTextView);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //This is for the menu to show up. Without this, it will never show.
        Log.d(TAG, "onCreateOptionsMenu: ");
        getMenuInflater().inflate(R.menu.second_act_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: ");

        if (item.getItemId() == R.id.saveIcon) {
            Toast.makeText(this, "Saving", Toast.LENGTH_SHORT).show();
            Intent data = new Intent(SecondActivity.this, MainActivity.class);
            hiddenTextView.setText(new Date().toString());
            data.putExtra("RET_TITLE", editText1.getText().toString());
            data.putExtra("RET_DESC", editText2.getText().toString());
            //data.putExtra("RET_TIME", currentTime);
            data.putExtra("RET_TIME",hiddenTextView.getText().toString());
            setResult(RESULT_OK, data);
            finish(); // This closes the current activity, returning us to the original activity
            return true;
        }
            //noteList.add(0, new Notes("title!","descriptu"));

            //recyclerView.setAdapter(nAdapter);
            //recyclerView.setLayoutManager(new LinearLayoutManager(this));

            //nAdapter.notifyDataSetChanged();
        return true; //In case anything goes wrong, returns nothing

    }


    @Override
    public void onBackPressed() {
        // Pressing the back arrow closes the current activity, returning us to the original activity
        /*
        Intent data = new Intent();
        data.putExtra("USER_TEXT_IDENTIFIER", editText1.getText().toString());
        data.putExtra("USER_TEXT_IDENTIFIER", editText2.getText().toString());
        setResult(RESULT_OK, data);
         */
        //Log.d(TAG, "onBackPressed: "+editText1.getText().toString());
        //Log.d(TAG, "onBackPressed: "+editText2.getText().toString());
        if((TextUtils.isEmpty(editText1.getText().toString())) && (TextUtils.isEmpty(editText2.getText().toString()))){
            Log.d(TAG, "onBackPressed: no entry");
            super.onBackPressed();
        }else {
            Log.d(TAG, "onBackPressed: not empty");

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage("Do you want to save your entries?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if user pressed "yes", then he is allowed to exit from application
                    //finish();
                    Toast.makeText(SecondActivity.this, "Saving", Toast.LENGTH_SHORT).show();
                    Intent data = new Intent(SecondActivity.this, MainActivity.class);
                    hiddenTextView.setText(new Date().toString());
                    data.putExtra("RET_TITLE", editText1.getText().toString());
                    data.putExtra("RET_DESC", editText2.getText().toString());
                    //data.putExtra("RET_TIME", currentTime);
                    data.putExtra("RET_TIME",hiddenTextView.getText().toString());
                    setResult(RESULT_OK, data);
                    finish(); // This closes the current activity, returning us to the original activity
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if user select "No", just cancel this dialog and continue with app
                    finish();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
            //super.onBackPressed(); //this will cause a window leak and crash. don't use this.
        }
    }
}
