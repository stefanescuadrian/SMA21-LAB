package com.example.hello_android;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText eName = findViewById(R.id.eName);
        Button bClick = findViewById(R.id.bClick);
        TextView tName = findViewById(R.id.tName);

        bClick.setOnClickListener(v -> {
            String text = "Hello, " + eName.getText() + "!";
            tName.setText(text);
        });
    }
}