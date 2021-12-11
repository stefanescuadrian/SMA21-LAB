package com.upt.cti.smartwallet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.upt.cti.smartwallet.R;
import com.upt.cti.smartwallet.model.Payment;
import com.upt.cti.smartwallet.ui.AddPaymentActivity;
import com.upt.cti.smartwallet.ui.AppState;
import com.upt.cti.smartwallet.ui.PaymentAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String MONTH = "Month";
    private static final String PREFERENCES = "preferences";

    private DatabaseReference databaseReference;
    private TextView tStatus;
    private Button btnPrevious;
    private Button btnNext;
    private FloatingActionButton fabAdd;
    private List<Payment> paymentsList = new ArrayList<>();
    private ListView paymentsListView;
    private int currentMonth;
    private SharedPreferences preferences;
    PaymentAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tStatus = (TextView) findViewById(R.id.tStatus);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnPrevious = (Button) findViewById(R.id.btnPrevious);
        preferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        currentMonth = preferences.getInt(MONTH, -1);
        fabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);
        adapter = new PaymentAdapter(this, R.layout.item_payment, paymentsList);
        paymentsListView = (ListView) findViewById(R.id.listPayments);
        paymentsListView.setAdapter(adapter);

        if (currentMonth == -1)
            currentMonth = Month.monthFromDate(AppState.getDate());

        fabAdd.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                v.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        switch (event.getActionMasked()){
                            case MotionEvent.ACTION_MOVE:
                                view.setX(event.getRawX() - 150);
                                view.setY(event.getRawY() - 420);
                                break;
                            case MotionEvent.ACTION_UP:
                                view.setOnTouchListener(null);
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });
                return true;
            }
        });

        // Setup for firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance("https://smart-wallet-bb6f8-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = database.getReference();
        AppState.get().setDatabaseReference(databaseReference);

        databaseReference.child("wallet").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (currentMonth == Month.monthFromDate(snapshot.getKey())){
                    Payment payment = snapshot.getValue(Payment.class);
                    payment.timestamp = snapshot.getKey();
                    paymentsList.add(payment);
                    adapter.notifyDataSetChanged();
                }
                if(paymentsList.isEmpty()){
                    tStatus.setText(String.format("No payment for %s ", Month.getMonthFromIndex(currentMonth)));
                }
                else {
                    tStatus.setText(String.format("Payments for %s...", Month.getMonthFromIndex(currentMonth)));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Payment payment = snapshot.getValue(Payment.class);
                payment.timestamp = snapshot.getKey();
                for (int i = 0;  i < paymentsList.size(); i++){
                    if (paymentsList.get(i).timestamp.equals(payment.timestamp)){
                        paymentsList.set(i, payment);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Payment payment = snapshot.getValue(Payment.class);
                payment.timestamp = snapshot.getKey();
                for (int i = 0;  i < paymentsList.size(); i++){
                    if (paymentsList.get(i).timestamp.equals(payment.timestamp)){
                        paymentsList.remove(i);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void clicked(View view) {
        switch (view.getId()){
            case R.id.fabAdd:
                Intent addIntent = new Intent(MainActivity.this, AddPaymentActivity.class);
                addIntent.putExtra("ACTION", "ADD");
                MainActivity.this.startActivity(addIntent);
                break;
            case R.id.btnPrevious:
                if (currentMonth == 0){ //reset from 11
                    currentMonth = 11;
                }
                else currentMonth--;
                preferences.edit().putInt(MONTH, currentMonth).apply();
                this.recreate();
                break;

            case R.id.btnNext:
                if (currentMonth == 11){ //reset from 0
                    currentMonth = 0;
                }
                else currentMonth++;
                preferences.edit().putInt(MONTH, currentMonth).apply();
                this.recreate();
                break;
        }
    }
}