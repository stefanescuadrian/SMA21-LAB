package com.upt.cti.smartwallet.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.upt.cti.smartwallet.R;
import com.upt.cti.smartwallet.model.Payment;
import com.upt.cti.smartwallet.model.PaymentType;

import java.io.IOException;
import java.util.List;

public class PaymentAdapter extends ArrayAdapter<Payment> {
    private Context context;
    private List<Payment> paymentsList;
    private int layoutID;

    public PaymentAdapter(Context context, int layoutID, List<Payment> paymentsList){
        super(context, layoutID, paymentsList);
        this.context = context;
        this.paymentsList = paymentsList;
        this.layoutID = layoutID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ItemHolder itemHolder;
        View view = convertView;

        if (view == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            itemHolder = new ItemHolder();

            view = inflater.inflate(layoutID, parent, false);
            itemHolder.tIndex = (TextView) view.findViewById(R.id.tIndex);
            itemHolder.tName = (TextView) view.findViewById(R.id.tName);
            itemHolder.lHeader = (RelativeLayout) view.findViewById(R.id.lHeader);
            itemHolder.tDate = (TextView) view.findViewById(R.id.tDate);
            itemHolder.tTime = (TextView) view.findViewById(R.id.tTime);
            itemHolder.tType = (TextView) view.findViewById(R.id.tType);
            itemHolder.iDelete = view.findViewById(R.id.imgDelete);
            itemHolder.iEdit = view.findViewById(R.id.imgEdit);
            itemHolder.tCost = view.findViewById(R.id.tCost);

            view.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) view.getTag();
        }

        final Payment paymentItem = paymentsList.get(position);

        itemHolder.tIndex.setText(String.valueOf(position + 1));
        itemHolder.tName.setText(paymentItem.getPaymentName());
        itemHolder.lHeader.setBackgroundColor(PaymentType.getColorFromPaymentType(paymentItem.getPaymentType()));
        itemHolder.tType.setText(paymentItem.getPaymentType());
        itemHolder.tCost.setText(String.format("%.2f RON", paymentItem.getPaymentCost()));
        itemHolder.tDate.setText("Date: " + paymentItem.timestamp.substring(0,10));
        itemHolder.tTime.setText("Time " + paymentItem.timestamp.substring(11));
        itemHolder.iDelete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(paymentItem!=null){
                    try {
                        delete(paymentItem);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(context,"Payment was deleted...", Toast.LENGTH_LONG).show();
                } else
                {
                    Toast.makeText(context,"This payment does not exist...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        itemHolder.iEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppState.get().setCurrentPayment(paymentItem);
                Intent editIntent = new Intent(context, AddPaymentActivity.class);
                editIntent.putExtra("ACTION", "EDIT");
                context.startActivity(editIntent);
            }
        });

        return view;
    }

    private void delete(Payment payment) throws IOException {
        DatabaseReference databaseReference = AppState.get().getDatabaseReference();
        if (databaseReference != null){
            databaseReference.child("wallet").child(payment.timestamp).removeValue();
        } else {
            AppState.get().updateLocalBackup(context, payment, false);
            this.remove(payment);
            this.notifyDataSetChanged();
        }

    }
}
