package com.upt.cti.smartwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import model.MonthlyExpenses;

public class MainActivity extends AppCompatActivity {

    private TextView tStatus;
    private EditText eSearch, eIncome, eExpenses;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tStatus = (TextView) findViewById(R.id.tStatus);
        eSearch = (EditText) findViewById(R.id.eSearch);
        eIncome = (EditText) findViewById(R.id.eIncome);
        eExpenses = (EditText) findViewById(R.id.eExpenses);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://smart-wallet-bb6f8-default-rtdb.europe-west1.firebasedatabase.app");
        database.setPersistenceEnabled(true);
        databaseReference = database.getReference();
    }

    public void clicked(View view){
        switch (view.getId()){
            case R.id.btnSearch:
                if (!eSearch.getText().toString().isEmpty()) {
                    System.out.println(databaseReference);
                    Log.d("SmartWallet", String.valueOf(databaseReference));
                    tStatus.setText("Searching...");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                String valueExpenses = snapshot.child("calendar").child(eSearch.getText().toString().toLowerCase()).child("expenses").getValue().toString();
                                String valueIncome = snapshot.child("calendar").child(eSearch.getText().toString().toLowerCase()).child("income").getValue().toString();
                                eIncome.setText(valueIncome);
                                eExpenses.setText(valueExpenses);
                                System.out.println(snapshot.child("calendar"));
                            }
                            catch (NullPointerException e)
                            {
                                tStatus.setText("No entries found for " + eSearch.getText().toString());
                                eIncome.setText(null);
                                eExpenses.setText(null);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            System.out.println("The read failed: " + error.getCode());
                        }
                    });

                }
                    //createNewDBListener();

                else {
                    Toast.makeText(this, "Search field may not be empty", Toast.LENGTH_LONG);
                }
                break;
            case R.id.btnUpdate:

                try{
                if (!eSearch.getText().toString().isEmpty() && !eIncome.getText().toString().isEmpty() && !eExpenses.getText().toString().isEmpty()) {
                    MonthlyExpenses monthlyExpenses = new MonthlyExpenses(eSearch.getText().toString(), eIncome.getText().toString(), eExpenses.getText().toString());
                    HashMap updateExpenses = new HashMap();
                    updateExpenses.put("income", monthlyExpenses.getIncome());
                    updateExpenses.put("expenses", monthlyExpenses.getExpenses());

                    databaseReference.child("calendar").child(eSearch.getText().toString()).updateChildren(updateExpenses).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            System.out.println("Successful!");
                        }
                    });
                }
                 else {
                    Toast.makeText(this, "Search field may not be empty", Toast.LENGTH_LONG);
                }}catch (NullPointerException e){
                    System.out.println("Unsuccessful!");

                }
                break;
        }
    }
}