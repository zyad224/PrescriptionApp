package com.example.zeyad.prescriptionapp.Adapters;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Zeyad on 7/3/2018.
 */

public class ListAdapterAddPres extends ArrayAdapter<String> {

    private ArrayList<String>tempList;

    public ListAdapterAddPres(@NonNull Context context, int resource, ArrayList<String> textViewResourceId) {
        super(context, resource, textViewResourceId);
        tempList=textViewResourceId;
    }


    public void updateList() {

        this.notifyDataSetChanged();
        Log.d("list", "updateList: "+tempList);

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        if (position % 2 == 0) {
            view.setBackgroundColor(Color.RED);
        } else {
            view.setBackgroundColor(Color.WHITE);
        }

        return view;
    }
}
