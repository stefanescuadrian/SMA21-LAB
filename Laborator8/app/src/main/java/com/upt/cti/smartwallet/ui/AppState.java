package com.upt.cti.smartwallet.ui;

import com.google.firebase.database.DatabaseReference;
import com.upt.cti.smartwallet.model.Payment;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppState {
    private static AppState singletonObject;

    public static AppState getSingletonObject() {
        return singletonObject;
    }

    public static void setSingletonObject(AppState singletonObject) {
        AppState.singletonObject = singletonObject;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public void setDatabaseReference(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public Payment getCurrentPayment() {
        return currentPayment;
    }

    public void setCurrentPayment(Payment currentPayment) {
        this.currentPayment = currentPayment;
    }

    private DatabaseReference databaseReference;
    private Payment currentPayment;

    public static synchronized AppState get() {
        if (singletonObject == null) {
            singletonObject = new AppState();
        }
        return singletonObject;
    }

    public static String getDate(){
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        return sdfDate.format(now);
    }


}
