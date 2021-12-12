package com.upt.cti.smartwallet;

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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.upt.cti.smartwallet.model.Payment;
import com.upt.cti.smartwallet.ui.AddPaymentActivity;
import com.upt.cti.smartwallet.ui.AppState;
import com.upt.cti.smartwallet.ui.PaymentAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OtherMainActivity extends AppCompatActivity {

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

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();

            if (user != null) {
                TextView tLoginDetail = findViewById(R.id.tLoginDetail);
                TextView tUser = findViewById(R.id.tUser);
                tLoginDetail.setText("Firebase ID: " + user.getUid());
                tUser.setText("Email: " + user.getEmail());

                AppState.get().setUser(user.getUid());
                attachDBListener(user.getUid());
            } else {
                startActivityForResult(new Intent(getApplicationContext(),
                        SignupActivity.class), REQ_SIGNIN);
            }
        };

        if (currentMonth == -1)
            currentMonth = Month.monthFromDate(AppState.getDate());

        final FirebaseDatabase database = FirebaseDatabase.getInstance("https://smart-wallet-bb6f8-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = database.getReference();

        paymentsListView.setOnItemClickListener((adapterView, view, i, l) -> {
            AppState.get().setCurrentPayment(paymentsList.get(i));
            startActivity(new Intent(getApplicationContext(), AddPaymentActivity.class));
        });

        databaseReference.child("wallet").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.println("onChild");
                Payment payment = dataSnapshot.getValue(Payment.class);
                if (payment != null) {
                    if(currentMonth == Month.monthFromDate(dataSnapshot.getKey())) {
                        payment.timestamp = dataSnapshot.getKey();
                        if (!paymentsList.contains(payment)) {
                            paymentsList.add(payment);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        if (!AppState.isNetworkAvailable(this)) {
            // has local storage already
            if (AppState.get().hasLocalStorage(this)) {
                paymentsList = AppState.get().loadFromLocalBackup(this, Month.getMonthFromIndex(currentMonth).toString());
                tStatus.setText("Found " + paymentsList.size() + " payments for " + Month.getMonthFromIndex(currentMonth) + ".");
            } else {
                Toast.makeText(this, "This app needs an internet connection!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

    }
    private void attachDBListener(String uid) {
        // setup firebase database
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();
        AppState.get().setDatabaseReference(databaseReference);

        databaseReference.child("wallet").child(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
            //...
        });
    }

    public void clicked(View view) {
        switch (view.getId()) {
            case R.id.btnSave:
                AppState.get().setDatabaseReference(databaseReference);
                AppState.get().setCurrentPayment(null);
                startActivity(new Intent(this, AddPaymentActivity.class));
                break;
            case R.id.btnNext:
                ++currentMonth;
                if(currentMonth == 12) currentMonth = 11;
                preferences.edit().putInt(MONTH, currentMonth).apply();
                System.out.println(Month.getMonthFromIndex(currentMonth));
                recreate();
                break;
            case R.id.btnPrevious:
                --currentMonth;
                if(currentMonth == -1) currentMonth = 0;
                preferences.edit().putInt(MONTH, currentMonth).apply();
                System.out.println(Month.getMonthFromIndex(currentMonth));
                recreate();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


}

