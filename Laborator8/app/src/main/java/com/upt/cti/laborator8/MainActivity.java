package com.upt.cti.laborator8;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.upt.cti.laborator8.ui.AddPaymentActivity;
import com.upt.cti.laborator8.ui.PaymentAdapter;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private int currentMonth;
    private List<Payment> payments = new ArrayList<>();
    private TextView tStatus;
    private Button bPrevious;
    private Button bNext;
    private ListView listPayments;
    FloatingActionButton fabAdd;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startActivity(getIntent());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tStatus = (TextView) findViewById(R.id.tStatus);
        bPrevious = (Button) findViewById(R.id.bPrevious);
        bNext = (Button) findViewById(R.id.bNext);
        fabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);
        listPayments = (ListView) findViewById(R.id.listPayments);
        final PaymentAdapter adapter = new PaymentAdapter(this, R.layout.item_payment, payments);
        listPayments.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://smart-wallet-bb6f8-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = database.getReference();
        //databaseReference.keepSynced(true);


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
        Date date = new Date();
        String sDate = sdf.format(date);

        //payments.add(new Payment(12.0, "pay1", "food", sDate));




        databaseReference.child("wallet").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                System.out.println("TEST" + snapshot.toString());


                double cost = Double.parseDouble(snapshot.child("cost").getValue().toString());
                String name = snapshot.child("name").getValue().toString();
                String type = snapshot.child("type").getValue().toString();
                String date = snapshot.getKey();

                payments.add(new Payment(cost,name,type,date));
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        listPayments.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                AppState.get().setCurrentPayment(payments.get(i));
                startActivity(new Intent(getApplicationContext(), AddPaymentActivity.class));
            }
        });
       adapter.notifyDataSetChanged();

    }

    public void clicked(View view) {
        switch(view.getId()) {
            case R.id.fabAdd:
                AppState.get().setCurrentPayment(null);
                startActivity(new Intent(this, AddNewPaymentActivity.class));
                break;
        }

    }
}