package com.example.assignment3;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import lifecycle.ActivityA;
import lifecycle.ActivityB;
import lifecycle.ActivityC;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @SuppressLint("NonConstantResourceId")
    public void clicked(View view) {
        switch (view.getId()){
            case R.id.btnA:
                startActivity(new Intent(this, ActivityA.class));
                break;
            case R.id.btnB:
                startActivity(new Intent(this, ActivityB.class));
                break;
            case R.id.btnC:
                startActivity(new Intent(this, ActivityC.class));
                break;
        }
    }
}