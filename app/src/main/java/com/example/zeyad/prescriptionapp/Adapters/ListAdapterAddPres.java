package com.example.zeyad.prescriptionapp.Adapters;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.zeyad.prescriptionapp.Database.Prescription;
import com.example.zeyad.prescriptionapp.R;

import java.util.ArrayList;

/**
 * Created by Zeyad on 7/3/2018.
 */

public class ListAdapterAddPres extends ArrayAdapter<Prescription> {

    private ArrayList<Prescription>tempList;

    public ListAdapterAddPres(@NonNull Context context, int resource, ArrayList<Prescription> textViewResourceId) {
        super(context, resource, textViewResourceId);
        tempList=textViewResourceId;
    }


    public void updateList() {

        this.notifyDataSetChanged();
        Log.d("list", "updateList: "+tempList);

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView viewItem = (TextView) super.getView(position, convertView, parent);
        if (position % 2 == 0)
            viewItem.setBackgroundColor(Color.RED);
         else
            viewItem.setBackgroundColor(Color.WHITE);

        viewItem.setText(getItem(position).getDoseTime()+"-"+getItem(position).getPrescriptionName()
                +"-"+getItem(position).getPrescriptionDoese()+" "+getItem(position).getPrescriptionType()
        +"             " +getContext().getString(R.string.cancelMark));

        return viewItem;
    }
}
