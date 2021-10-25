package lifecycle;
import com.example.assignment3.R;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ActivityA extends AppCompatActivity {
    public static final String TAG = "A";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activitya);
        setTitle("A");
        Log.d(TAG, "onCreate A");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(ActivityA.TAG, "onResume A");
    }

    @SuppressLint("NonConstantResourceId")
    public void clicked(View view) {
        switch (view.getId()){
            case R.id.btnA:
                startActivity(new Intent(this, ActivityA.class));
                break;
            case R.id.btnB:
                startActivity(new Intent(this, ActivityB.class));
                break;
            case R.id.btnC:
                startActivity(new Intent(this, ActivityC.class));
                break;
        }
    }


}