package com.example.assignment5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "";
    private static int notificationId=1;
    MyBroadcastReceiver batteryBroadcast;
    PowerConnectionReceiver powerConnectionReceiver;
    IntentFilter intentFilter;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        intentFilterAndBroadcast();
    }


    public void updateTextView(boolean s) {
        TextView textView = findViewById(R.id.textView);
        textView.setText(String.valueOf(s));
    }


    private void intentFilterAndBroadcast() {
        intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryBroadcast = new MyBroadcastReceiver(){
            @SuppressLint("SetTextI18n")
            @Override
            public void onReceive(Context context, Intent intent){
                if(Intent.ACTION_BATTERY_CHANGED.equals(Intent.ACTION_BATTERY_CHANGED)){
                    textView.setText(String.valueOf(intent.getIntExtra("level",-1)) + "%");
                    int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,-1);
                    boolean isCharging = (status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL);
                    updateTextView(isCharging);
                }
            }
        };

        powerConnectionReceiver = new PowerConnectionReceiver(){
            @Override
            public void onReceive(Context context, Intent intent){
                Intent newIntent = new Intent(context, MainActivity.class);
                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,-1);
                boolean isCharging = (status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL);
                newIntent.putExtra("status", isCharging);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 , newIntent, 0);

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Charging status changed")
                    //.setContentText(isCharging)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(MainActivity.notificationId, mBuilder.build());

            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(batteryBroadcast, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(batteryBroadcast);
    }
}
