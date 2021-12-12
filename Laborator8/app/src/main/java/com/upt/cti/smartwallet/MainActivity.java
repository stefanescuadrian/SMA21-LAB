package com.upt.cti.smartwallet;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String MONTH = "Month";
    private static final String PREFERENCES = "preferences";
    private static final int REQ_SIGNIN = 3;

    private DatabaseReference databaseReference;
    private TextView tStatus;
    private Button btnPrevious;
    private Button btnNext;
    private FloatingActionButton fabAdd;
    private List<Payment> paymentsList = new ArrayList<>();
    private ListView paymentsListView;
    private int currentMonth;
    private SharedPreferences preferences;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    PaymentAdapter adapter;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


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

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            loadLocal();
                        }
                    }
                });

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

        if (!AppState.isNetworkAvailable(this)) {
            loadLocal();
        } else {
            // setup firebase
            final FirebaseDatabase database = FirebaseDatabase.getInstance("https://smart-wallet-bb6f8-default-rtdb.europe-west1.firebasedatabase.app/");
            databaseReference = database.getReference();
            AppState.get().setDatabaseReference(databaseReference);
            for (Month month : Month.values()) {
                List<Payment> localPayments = AppState.get().loadFromLocalBackup(MainActivity.this, Month.getMonthStringFromIndex(month));
                for (Payment paymentItm : localPayments) {
                    databaseReference.child("wallet").child(paymentItm.timestamp).setValue(paymentItm);
                }
            }

            databaseReference.child("wallet").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Payment payment = snapshot.getValue(Payment.class);
                    payment.timestamp = snapshot.getKey();
                    try {
                        AppState.get().updateLocalBackup(MainActivity.this, payment, true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (currentMonth == Month.monthFromDate(snapshot.getKey())) {
                        paymentsList.add(payment);
                        adapter.notifyDataSetChanged();
                    }
                    if (paymentsList.isEmpty()) {
                        tStatus.setText(String.format("No payment for %s ", Month.getMonthFromIndex(currentMonth)));
                    } else {
                        tStatus.setText(String.format("Payments for %s...", Month.getMonthFromIndex(currentMonth)));
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Payment payment = snapshot.getValue(Payment.class);
                    payment.timestamp = snapshot.getKey();
                    for (int i = 0; i < paymentsList.size(); i++) {
                        if (paymentsList.get(i).timestamp.equals(payment.timestamp)) {
                            paymentsList.set(i, payment);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    try {
                        AppState.get().updateLocalBackup(MainActivity.this, payment, false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        AppState.get().updateLocalBackup(MainActivity.this, payment, true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    Payment payment = snapshot.getValue(Payment.class);
                    payment.timestamp = snapshot.getKey();
                    for (int i = 0; i < paymentsList.size(); i++) {
                        if (paymentsList.get(i).timestamp.equals(payment.timestamp)) {
                            paymentsList.remove(i);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    try {
                        AppState.get().updateLocalBackup(MainActivity.this, payment, false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    protected  void onActivityResult (int requestCode, int resultCord, Intent data) {
        super.onActivityResult(requestCode, resultCord, data);
        if (requestCode == REQ_SIGNIN) {
            if (requestCode == RESULT_OK) {
                //get data from intent
                String user = data.getStringExtra("user");
                String pass = data.getStringExtra("pass");
                //...
            } else if (resultCord == RESULT_CANCELED) {
                // data was not retrived
            }
        }
    }
    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if (mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void loadLocal(){
            if (AppState.get().hasLocalStorage(this)) {
                paymentsList = AppState.get().loadFromLocalBackup(MainActivity.this, Month.getMonthFromIndex(currentMonth).toString());
                tStatus.setText("Found " + paymentsList.size() + " payments for " +
                        Month.getMonthFromIndex(currentMonth) + ".");
                adapter.clear();
                adapter.addAll(paymentsList);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Need internet connection!", Toast.LENGTH_SHORT).show();
                return;
            }
    }


    public void clicked(View view) {
        switch (view.getId()){
            case R.id.fabAdd:
                Intent addIntent = new Intent(MainActivity.this, AddPaymentActivity.class);
                addIntent.putExtra("ACTION", "ADD");
                activityResultLauncher.launch(addIntent);
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