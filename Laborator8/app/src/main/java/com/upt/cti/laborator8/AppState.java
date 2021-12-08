package com.upt.cti.laborator8;

import android.provider.ContactsContract;

import com.google.firebase.database.DatabaseReference;

public class AppState {
    private static AppState singletonObject;

    public static synchronized AppState get(){
        if(singletonObject == null){
            singletonObject = new AppState();
        }
        return singletonObject;
    }
    private DatabaseReference databaseReference;
    private Payment currentPayment;

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

}
