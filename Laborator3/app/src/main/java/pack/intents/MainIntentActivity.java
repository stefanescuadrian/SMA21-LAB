package pack.intents;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment3.R;

public class MainIntentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_intent);


        try {


            Button button3 = findViewById(R.id.button3);
            button3.setOnClickListener(v -> {
                String my_action = "MSA.LAUNCH";
                Uri webpage = Uri.parse("http://www.google.com");
                Intent i = new Intent(my_action, webpage);
                startActivity(i);
            });

            Button button4 = findViewById(R.id.button4);
            button4.setOnClickListener(v -> {
                String my_action = "MSA.LAUNCH";
                Uri tel = Uri.parse("tel:00401213456");
                Intent i = new Intent(my_action, tel);
                startActivity(i);
            });

        }
        catch (Exception e){
            TextView textView = findViewById(R.id.textView);
            Uri url = getIntent().getData();
            textView.setText(url.toString());
        }
    }

    public void clicked1(View v){
        Uri webpage = Uri.parse("https://www.google.com");
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(webIntent);
    }
    public void clicked2(View v){
        Uri tel = Uri.parse("tel:00401213456");
        Intent callIntent = new Intent(Intent.ACTION_VIEW, tel);
        startActivity(callIntent);
    }

}