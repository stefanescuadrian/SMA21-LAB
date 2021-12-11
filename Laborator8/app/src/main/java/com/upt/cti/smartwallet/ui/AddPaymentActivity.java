package com.upt.cti.smartwallet.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.upt.cti.smartwallet.R;
import com.upt.cti.smartwallet.model.Payment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AddPaymentActivity extends AppCompatActivity {

        private DatabaseReference databaseReference;
        private EditText ePaymentCost;
        private EditText ePaymentName;
        private Spinner sPaymentType;
        private Button btnSave;
        private List<String> paymentTypes;



    private enum Actions{
            EDIT,
            ADD
        }
        private Actions action;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);

        Intent intent = getIntent();
        String action = intent.getStringExtra("ACTION");
        databaseReference = AppState.get().getDatabaseReference();

        ePaymentName = findViewById(R.id.ePaymentName);
        ePaymentCost = findViewById(R.id.eCost);
        sPaymentType = findViewById(R.id.sPaymentType);
        btnSave = findViewById(R.id.btnSave);
        paymentTypes = Arrays.asList("food", "entertainment", "travel", "taxes");
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, paymentTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sPaymentType.setAdapter(adapter);
        if (action.equals("EDIT")){
            this.action = Actions.EDIT;
            Payment payment = AppState.get().getCurrentPayment();
            btnSave.setText("UPDATE");
            ePaymentName.setText(payment.getPaymentName());
            ePaymentCost.setText(String.format("%.2f", payment.getPaymentCost()));
            for (int i = 0; i <paymentTypes.size(); i++){
                if (paymentTypes.get(i).equals(payment.getPaymentType())){
                    sPaymentType.setSelection(i);
                }
            }
        } else if (action.equals("ADD")){
            this.action = Actions.ADD;
            btnSave.setText("ADD");
        }
    }

    private Payment generateNewPayment(){
        String paymentName;
        float paymentCost;
        String paymentType;

        if (!ePaymentName.getText().toString().isEmpty()) {
            paymentName = ePaymentName.getText().toString();
        } else {
            Toast.makeText(this, "Name field is empty!!", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (!ePaymentCost.getText().toString().isEmpty()) {
            String eCostText = ePaymentCost.getText().toString();
            try {
                paymentCost = Float.parseFloat(eCostText);
            } catch (NumberFormatException exception) {
                Toast.makeText(this, "Need a number", Toast.LENGTH_SHORT).show();
                return null;
            }
        } else {
            Toast.makeText(this, "Cost is empty!!", Toast.LENGTH_SHORT).show();
            return null;
        }
        paymentType = sPaymentType.getSelectedItem().toString();
        Payment payment = new Payment(null, paymentCost, paymentName, paymentType);
        return payment;
    }

    private Payment getEditedPayment(Payment payment){
        String paymentName;
        float paymentCost;
        String paymentType;
        if (!ePaymentName.getText().toString().isEmpty()) {
            paymentName = ePaymentName.getText().toString();
        } else {
            Toast.makeText(this, "Name field is empty!!", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (!ePaymentCost.getText().toString().isEmpty()) {
            String eCostText = ePaymentCost.getText().toString();
            try {
                paymentCost = Float.parseFloat(eCostText);
            } catch (NumberFormatException exception) {
                Toast.makeText(this, "Need a number for cost!!", Toast.LENGTH_SHORT).show();
                return null;
            }
        } else {
            Toast.makeText(this, "Cost is empty!!", Toast.LENGTH_SHORT).show();
            return null;
        }
        paymentType = sPaymentType.getSelectedItem().toString();
        payment.setPaymentName(paymentName);
        payment.setPaymentCost(paymentCost);
        payment.setPaymentType(paymentType);
        return payment;
    }
    private void add() throws IOException {
        Payment payment = generateNewPayment();
        String dateTime = getDate();
            if(payment != null){
                payment.timestamp = dateTime;
                if (databaseReference != null) {
                databaseReference.child("wallet").child(dateTime).setValue(payment);
            } else {
                    Toast.makeText(this, "No database connected", Toast.LENGTH_SHORT).show();
                    AppState.get().updateLocalBackup(this,payment,true);
                }
                setResult(RESULT_OK);
                finish();
                Toast.makeText(this, "Payment added", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getDate(){
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        return sdfDate.format(now);
    }

    private void edit() throws IOException {
            Payment payment = getEditedPayment(AppState.get().getCurrentPayment());
            if(payment != null) {
                if (databaseReference != null) {
                    databaseReference.child("wallet").child(payment.timestamp).setValue(payment);
                } else {
                    Toast.makeText(this, "No database connected", Toast.LENGTH_SHORT).show();
                    AppState.get().updateLocalBackup(this, payment, false);
                    AppState.get().updateLocalBackup(this, payment, true);
                }
                setResult(RESULT_OK);
                finish();
                Toast.makeText(this, "Payment updated", Toast.LENGTH_SHORT).show();
            }
    }
    public void clicked(View view) throws IOException {
        switch (view.getId()){
            case R.id.btnSave:
                if(action == Actions.ADD){
                    add();
                }
                else if (action == Actions.EDIT){
                    edit();
                }
                break;
        }
    }
}
