package com.upt.cti.smartwallet.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.upt.cti.smartwallet.Month;
import com.upt.cti.smartwallet.model.Payment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public void updateLocalBackup(Context context, Payment payment, boolean toAdd) throws IOException {
        String filename = payment.timestamp;
try{
        if (toAdd){
            FileOutputStream fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(payment.recreatePayment());
            objectOutputStream.close();
            fileOutputStream.close();
        } else {
            context.deleteFile(filename);
        }
    } catch (IOException e){
        Toast.makeText(context, "Local data is not accessed.", Toast.LENGTH_SHORT).show();
    }
    }

    public boolean hasLocalStorage(Context context){
        return context.getFilesDir().listFiles().length > 0;
    }

    private boolean hasGoodFormat(String dateStr) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public List<Payment> loadFromLocalBackup(Context context, String month){
        try {
            List<Payment> payments = new ArrayList<>();

            for (File file : context.getFilesDir().listFiles()) {
                if (hasGoodFormat(file.getName())) {
                    FileInputStream fis = context.openFileInput(file.getName());
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    Payment payment = (Payment) ois.readObject();
                    if (payment.getTimestamp() == getDate())
                        payments.add(payment);
                    fis.close();
                    ois.close();
                }
            }
            return payments;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =

                (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public String getTimeStamp(){
        return currentPayment.timestamp;
    }

    public String getUser(){
        return currentPayment.getUser();
    }

    public void setUser(String user){
        currentPayment.setUser(user);
    }

}
