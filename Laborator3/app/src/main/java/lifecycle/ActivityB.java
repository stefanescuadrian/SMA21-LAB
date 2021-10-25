package lifecycle;
import com.example.assignment3.R;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ActivityB extends AppCompatActivity {
    public static final String TAG = "B";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activityb);
        setTitle("B");
        Log.d(TAG, "onCreate B");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(ActivityA.TAG, "onResume B");
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