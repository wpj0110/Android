package com.example.tipsplitcalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    static double total;

    private static final String TAG = "MainActivityTag"; //type logt then Tab
    private EditText inputText;
    private EditText inputNumPeople;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private TextView answerTipAmount;
    private TextView answerTotal;
    private TextView answerDivideBill;
    private TextView answerOverage;
    private Button buttonNumPeople;

    public Double roundUp(double num){ //Increments by 1 if necessary
        Log.d(TAG, "roundUp: ");
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        String x = df.format(num);
        double num2 = Double.parseDouble(x);
        Log.d(TAG, "roundUp: num is "+num+" num2 is " +num2);
        if ((num2-num) != 0){
            Log.d(TAG, "roundUp: increments");
            if (num2 > num){
                return num2; //it is already incremented, no need to add
            }
            else{
                return num2 + 0.01; //it's not yet incremented so it is done here.
            }
        }
        else{
            Log.d(TAG, "roundUp: doesn't increment");
            return num;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: I'm completing my onCreate "); //type logd then Tab, great for debugging
        //roundUp(1.243);
        inputText = findViewById(R.id.inputBill);
        radioGroup = findViewById(R.id.radioTipPercents);
        answerTipAmount = findViewById(R.id.answerTipAmount);
        answerTotal = findViewById(R.id.answerTotal);
        inputNumPeople = findViewById(R.id.inputNumPeople);
        buttonNumPeople = findViewById(R.id.buttonNumPeople);
        answerDivideBill = findViewById(R.id.answerDivideBill);
        answerOverage = findViewById(R.id.answerOverage);
    }
    
    public void calculateTip(View v){
        Log.d(TAG, "calculateTip: ");
        double value,tip;

        String valueStr = inputText.getText().toString(); //gets inputText, turns it into string
        if(TextUtils.isEmpty(valueStr)){ //If there is no input: sets the value to zero
            Log.d(TAG, "calculateTip: it is a blank space");
            value = 0;
        }
        else {
            value = Double.parseDouble(valueStr); //converts it back to int
            Log.d(TAG, "calculateTip: Value is " + value);
        }

        ////Section below is for radio buttons////
        if(v.getId() == R.id.radioPercent12){
            Log.d(TAG, "calculateTip: radioPercent12 selected");
            tip = value*0.12;
            tip = roundUp(tip);
            total = tip+value;
            //Log.d(TAG, "calculateTip: Tip and Total: "+tip +" "+ total);
            answerTipAmount.setText(String.format("$%.2f",tip));
            answerTotal.setText(String.format("$%.2f",total));
        }
        else if(v.getId() == R.id.radioPercent15){
            Log.d(TAG, "calculateTip: radioPercent15 selected");
            tip = value*0.15;
            tip = roundUp(tip);
            total = tip+value;
            //Log.d(TAG, "calculateTip: Tip and Total: "+tip +" "+ total);
            answerTipAmount.setText(String.format("$%.2f",tip));
            answerTotal.setText(String.format("$%.2f",total));
        }
        else if(v.getId() == R.id.radioPercent18){
            Log.d(TAG, "calculateTip: radioPercent18 selected");
            tip = value*0.18;
            tip = roundUp(tip);
            total = tip+value;
            //Log.d(TAG, "calculateTip: Tip and Total: "+tip +" "+ total);
            answerTipAmount.setText(String.format("$%.2f",tip));
            answerTotal.setText(String.format("$%.2f",total));
        }
        else if(v.getId() == R.id.radioPercent20){
            Log.d(TAG, "calculateTip: radioPercent20 selected");
            tip = value*0.2;
            tip = roundUp(tip);
            total = tip+value;
            //Log.d(TAG, "calculateTip: Tip and Total: "+tip +" "+ total);
            answerTipAmount.setText(String.format("$%.2f",tip));
            answerTotal.setText(String.format("$%.2f",total));
        }
        ////Section Above is for Radio Buttons////

    }
    public void billDivide(View v){
        Log.d(TAG, "billDivide: ");
        int numPeople;
        double quotient,overage;
        String valueStr = inputNumPeople.getText().toString(); //gets inputText, turns it into string
        if(TextUtils.isEmpty(valueStr)){ //If there is no input: sets the value to one
            Log.d(TAG, "billDivide: it is a blank space");
            numPeople = 1;
        }
        else {
            numPeople = Integer.parseInt(valueStr); //converts it back to int
            Log.d(TAG, "billDivide: Number of people is " + numPeople);
        }
        String valueStr2 = answerTotal.getText().toString();
        quotient = total/numPeople;
        quotient = roundUp(quotient);
        overage = (quotient*numPeople)-total;
        answerDivideBill.setText(String.format("$%.2f",quotient));
        answerOverage.setText(String.format("$%.2f",overage));
        Log.d(TAG, "billDivide: total is "+total+" NumPeople is "+numPeople+" Quotient is "+quotient +" Overage is "+overage);
    }

    public void clearAll(View v){
        Log.d(TAG, "clearAll: ");
        inputText.setText("");
        answerTipAmount.setText("");
        answerTotal.setText("");
        inputNumPeople.setText("");
        answerDivideBill.setText("");
        answerOverage.setText("");
        radioGroup.clearCheck();
    }
    

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: ");

        String saveTip = answerTipAmount.getText().toString();
        String saveTotal = answerTotal.getText().toString();
        String saveDivide = answerDivideBill.getText().toString();
        String saveOverage = answerOverage.getText().toString();

        outState.putString("TIP AMOUNT", saveTip);
        outState.putString("TOTAL AMOUNT",saveTotal);
        outState.putString("DIVIDED AMOUNT",saveDivide);
        outState.putString("OVERAGE",saveOverage);
        //outState.putDouble("TOTAL",total);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState: ");

        answerTipAmount.setText(savedInstanceState.getString("TIP AMOUNT"));
        answerTotal.setText(savedInstanceState.getString("TOTAL AMOUNT"));
        answerDivideBill.setText((savedInstanceState.getString("DIVIDED AMOUNT")));
        answerOverage.setText(savedInstanceState.getString("OVERAGE"));
    }
}