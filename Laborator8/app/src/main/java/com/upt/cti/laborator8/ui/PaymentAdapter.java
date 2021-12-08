package com.upt.cti.laborator8.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.upt.cti.laborator8.Payment;
import com.upt.cti.laborator8.R;

import java.util.List;

public class PaymentAdapter extends ArrayAdapter<Payment> {
    private Context context;
    private List<Payment> payments;
    private int layoutResID;

    public PaymentAdapter(Context context, int layoutResourceID, List<Payment> payments){
        super(context, layoutResourceID, payments);
        this.context = context;
        this.layoutResID = layoutResourceID;
        this.payments = payments;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ItemHolder itemHolder;
        View view = convertView;
        final Payment pItem = payments.get(position);

        if (view == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            itemHolder = new ItemHolder();

            view = inflater.inflate(layoutResID, parent, false);
            itemHolder.tIndex = (TextView) view.findViewById(R.id.tIndex);
            itemHolder.tName = (TextView) view.findViewById(R.id.tName);
            itemHolder.lHeader = (RelativeLayout) view.findViewById(R.id.lHeader);
            itemHolder.tDate = (TextView) view.findViewById(R.id.tDate);
            itemHolder.tTime = (TextView) view.findViewById(R.id.tTime);
            itemHolder.tCost = (TextView) view.findViewById(R.id.tCost);
            itemHolder.tType = (TextView) view.findViewById(R.id.tType);
            itemHolder.iEdit = (ImageView) view.findViewById(R.id.imgEdit);
            itemHolder.iDelete = (ImageView) view.findViewById(R.id.imgDelete);

            view.setTag(itemHolder);


            itemHolder.tName.setText(pItem.getName());
        }
        else
        {
            itemHolder = (ItemHolder) view.getTag();
        }



        itemHolder.tIndex.setText(String.valueOf(position+1));
        itemHolder.tName.setText(pItem.getName());
        itemHolder.lHeader.setBackgroundColor(PaymentType.getColorFromPaymentType(pItem.getType()));
        itemHolder.tCost.setText(pItem.getCost() + " LEI");
        itemHolder.tType.setText(pItem.getType());
        itemHolder.tDate.setText("Date: " + pItem.timestamp.substring(0, 10));
        itemHolder.tTime.setText("Time: " + pItem.timestamp.substring(11));
        itemHolder.iEdit.bringToFront();
        itemHolder.iEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.printf("testtesttest");
            }
        });
                itemHolder.iDelete.bringToFront();
        itemHolder.iDelete.setOnClickListener(view12 -> {
            // delete payment at position
        });
        return  view;

    }




    private static class ItemHolder{
        TextView tIndex;
        TextView tName;
        RelativeLayout lHeader;
        TextView tDate, tTime;
        TextView tCost, tType;
        ImageView iEdit, iDelete;
    }

}
