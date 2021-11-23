package com.upt.cti.smartwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import model.MonthlyExpenses;

public class MainActivity extends AppCompatActivity {

    private TextView tStatus;
    private EditText eSearch, eIncome, eExpenses;
    private Spinner spinner;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tStatus = (TextView) findViewById(R.id.tStatus);
        eIncome = (EditText) findViewById(R.id.eIncome);
        eExpenses = (EditText) findViewById(R.id.eExpenses);
        spinner = (Spinner) findViewById(R.id.spinner);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://smart-wallet-bb6f8-default-rtdb.europe-west1.firebasedatabase.app");
        database.setPersistenceEnabled(true);
        databaseReference = database.getReference();

        databaseReference.child("calendar").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final List<MonthlyExpenses> monthlyExpensesList = new ArrayList<>();
                final List <String> monthNames = new ArrayList<>();
                monthNames.add("Select month");

                for (DataSnapshot eachSnapShot : snapshot.getChildren()) {
                    monthNames.add(eachSnapShot.getKey());
                    System.out.println("PRINT EACH SNAPSHOT" + eachSnapShot);
                }
                final ArrayAdapter<String> sAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, monthNames);

                sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(sAdapter);

               // monthlyExpenses.setExpenses(snapshot.child("expenses").getValue().toString());
                System.out.println(spinner.getSelectedItem().toString());

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (spinner.getSelectedItem().toString() != "Select month") {
                            eIncome.setText(snapshot.child(spinner.getSelectedItem().toString()).child("income").getValue().toString());
                            eExpenses.setText(snapshot.child(spinner.getSelectedItem().toString()).child("expenses").getValue().toString());
                        }
                        else {
                            eIncome.setText(null);
                            eExpenses.setText(null);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    public void clicked(View view){
        switch (view.getId()){
            case R.id.btnUpdate:

//                try{
//                if (!eIncome.getText().toString().isEmpty() && !eExpenses.getText().toString().isEmpty()) {
//                    HashMap updateExpenses = new HashMap();
//                    updateExpenses.put("income", monthlyExpenses.getIncome());
//                    updateExpenses.put("expenses", monthlyExpenses.getExpenses());
//
//                    databaseReference.child("calendar").child(eSearch.getText().toString()).updateChildren(updateExpenses).addOnCompleteListener(new OnCompleteListener() {
//                        @Override
//                        public void onComplete(@NonNull Task task) {
//                            System.out.println("Successful!");
//                        }
//                    });
//                }
//                 else {
//                    Toast.makeText(this, "Search field may not be empty", Toast.LENGTH_LONG);
//                }}catch (NullPointerException e){
//                    System.out.println("Unsuccessful!");
//
//                }
                break;
        }
    }
}