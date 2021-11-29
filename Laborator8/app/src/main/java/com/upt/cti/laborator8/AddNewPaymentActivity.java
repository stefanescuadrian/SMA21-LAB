package com.upt.cti.laborator8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class AddNewPaymentActivity extends AppCompatActivity {

    private EditText eCost;
    private EditText eName;
    private EditText eType;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_payment);
        eCost = (EditText) findViewById(R.id.eCost);
        eName = (EditText) findViewById(R.id.eName);
        eType = (EditText) findViewById(R.id.eType);
        final FirebaseDatabase database = FirebaseDatabase.getInstance("https://smart-wallet-bb6f8-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = database.getReference();
       // databaseReference.keepSynced(true);
    }

    public void clicked(View view) {

        switch(view.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.okButton:
                if (!eCost.toString().isEmpty() && !eName.toString().isEmpty() && !eType.toString().isEmpty()){
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            double cost = Double.parseDouble(eCost.getText().toString());
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                            Date date = new Date();
                            String sDate = sdf.format(date);
                            Payment p = new Payment(cost, eName.getText().toString(), eType.getText().toString());

                           // HashMap newPayment = new HashMap();
                            System.out.println(sDate);
                           // newPayment.put(sDate,"");

                            //System.out.println(newPayment);

                            databaseReference.child("wallet").child(sDate).setValue(p);
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            finish();
                        }
                    });

                }
                break;
        }
    }
}