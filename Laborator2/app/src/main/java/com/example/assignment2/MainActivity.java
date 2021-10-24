package com.example.assignment2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HashMap<String, Integer> mapColors = new HashMap<>();
        mapColors.put("Red",R.color.red);
        mapColors.put("Green",R.color.green);
        mapColors.put("Blue",R.color.blue);

        Button positiveButton = findViewById(R.id.positiveButton);
        Button negativeButton = findViewById(R.id.negativeButton);
        Button sayHelloButton = findViewById(R.id.bClick);


        Spinner spinner = findViewById(R.id.colorSpinner);
        ArrayList<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("Red");
        spinnerArray.add("Green");
        spinnerArray.add("Blue");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,spinnerArray);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item.toString().equals("Red")) {
                    sayHelloButton.setBackgroundTintList(null);
                    sayHelloButton.setBackgroundColor(getResources().getColor(R.color.red));
                    positiveButton.setBackgroundTintList(null);
                    positiveButton.setBackgroundColor(getResources().getColor(R.color.red));
                    negativeButton.setBackgroundTintList(null);
                    negativeButton.setBackgroundColor(getResources().getColor(R.color.red));
                }
                if (item.toString().equals("Green")) {
                    sayHelloButton.setBackgroundTintList(null);
                    sayHelloButton.setBackgroundColor(getResources().getColor(R.color.green));
                    positiveButton.setBackgroundTintList(null);
                    positiveButton.setBackgroundColor(getResources().getColor(R.color.green));
                    negativeButton.setBackgroundTintList(null);
                    negativeButton.setBackgroundColor(getResources().getColor(R.color.green));
                }
                if (item.toString().equals("Blue")) {
                    sayHelloButton.setBackgroundTintList(null);
                    sayHelloButton.setBackgroundColor(getResources().getColor(R.color.blue));
                    positiveButton.setBackgroundTintList(null);
                    positiveButton.setBackgroundColor(getResources().getColor(R.color.blue));
                    negativeButton.setBackgroundTintList(null);
                    negativeButton.setBackgroundColor(getResources().getColor(R.color.blue));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    @SuppressLint("ClickableViewAccessibility")
    public void onButtonHelloClick(View v) {
        EditText eName = findViewById(R.id.eName);
        TextView tName = findViewById(R.id.tName);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View popupView = inflater.inflate(R.layout.popup_window, null);
        TextView popupTextView = popupView.findViewById(R.id.popupTextView);

        String text = "Hello, " + eName.getText() + "!";
        popupTextView.setText(text);
        tName.setText(text);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        //show the popup window
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);


        //dismiss the popup window when touched
        popupView.setOnTouchListener((v1, event) -> {
            popupWindow.dismiss();
            return true;
        });
    }

    public void onButton1Click(View view){
        Context context = getApplicationContext();
        CharSequence text = "Button1 pressed...";
        int duration = Toast.LENGTH_SHORT;

        @SuppressLint("ShowToast") Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void onButton2Click(View v){
        Context context = getApplicationContext();
        CharSequence text = "Button2 pressed...";
        int duration = Toast.LENGTH_SHORT;

        @SuppressLint("ShowToast") Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }



}


