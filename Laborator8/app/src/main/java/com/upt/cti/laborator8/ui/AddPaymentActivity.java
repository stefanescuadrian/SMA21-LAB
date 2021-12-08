package com.upt.cti.laborator8.ui;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.upt.cti.laborator8.AppState;
import com.upt.cti.laborator8.Payment;
import com.upt.cti.laborator8.R;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;


public class AddPaymentActivity extends AppCompatActivity {
    Payment payment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);

        setTitle("Add or edit payment");
        //ui
        EditText eName = (EditText) findViewById(R.id.eName);
        EditText eCost = (EditText) findViewById(R.id.eCost);
        Spinner sType = (Spinner) findViewById(R.id.spinnerID);
        TextView tTimeStamp = (TextView) findViewById(R.id.tTimeStamp);

        //spinner adapter
        String[] types = PaymentType.getTypes();
        final ArrayAdapter<String> sAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,types);
        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sType.setAdapter(sAdapter);

        //initialize UI if editing
        payment = AppState.get().getCurrentPayment();
        if (payment != null){
            eName.setText(payment.getName());
            eCost.setText(String.valueOf(payment.getCost()));
            tTimeStamp.setText("Time of payment: " + payment.timestamp);

            try{
                sType.setSelection(Arrays.asList(types).indexOf(payment.getType()));
            } catch (Exception e){

            }
        }
            else {
                tTimeStamp.setText("");
            }
        }

        public static String getCurrentTimeDate(){
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            return sdfDate.format(now);
        }

    public void clicked(View view) {
        switch (view.getId()){
            case R.id.btnSave:
                if (payment != null)
                    //save(payment.timestamp);
                    System.out.println("yes");
                else
                    //save(AppState.getCurrentTimeDate());
                    System.out.println("no");
                break;

            case R.id.btnDelete:
                if (payment != null)
                    //delete(payment.timestamp);
                    System.out.println("yes");
                else
                    Toast.makeText(this, "Payment does not exist", Toast.LENGTH_SHORT).show();
                break;
        }
    }



    }

