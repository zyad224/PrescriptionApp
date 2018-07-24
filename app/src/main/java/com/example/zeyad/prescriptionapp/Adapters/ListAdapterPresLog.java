package com.example.zeyad.prescriptionapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.zeyad.prescriptionapp.Database.AppDatabase;
import com.example.zeyad.prescriptionapp.Database.Notification;
import com.example.zeyad.prescriptionapp.Database.Prescription;
import com.example.zeyad.prescriptionapp.Database.User;
import com.example.zeyad.prescriptionapp.Fragments.PrescriptionLog;
import com.example.zeyad.prescriptionapp.MainActivity;
import com.example.zeyad.prescriptionapp.R;
import com.example.zeyad.prescriptionapp.SigninActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.app.PendingIntent.getActivity;

/**
 * Created by Zeyad on 7/12/2018.
 */

public class ListAdapterPresLog extends ArrayAdapter<Prescription> {

    private List<Prescription> list;
    private  ArrayList<Prescription>copyList;
    private ProgressDialog progressDialog;
    private Button delete;

    public static int counter=0;
    public ListAdapterPresLog(@NonNull Context context, int resource, List<Prescription> textViewResourceId) {
        super(context, resource, textViewResourceId);
        list=textViewResourceId;
        this.copyList=new ArrayList<Prescription>();

    }


    public void updateList() {
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view=convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.pescripiton_log_element, null);
        }

        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position).getPrescriptionName() +"-"+
                list.get(position).getPrescriptionType());

        drawPrescriptionImage(listItemText,list.get(position).getPrescriptionType());


        delete = (Button)view.findViewById(R.id.delete);
        delete.setBackgroundColor(Color.BLACK);
        delete.setTextColor(Color.WHITE);
        final int positionToRemove = position;
        final Integer positionToRemoveDb=position;
        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                new delPrescAsyncTask().execute(list.get(positionToRemove).getPrescriptionName());
                list.remove(positionToRemove);
                notifyDataSetChanged();

            }
        });

        return view;
    }

    private void drawPrescriptionImage(TextView listItemText, String prescriptionType){

        switch(prescriptionType){
            case("Pills"):
                listItemText.setCompoundDrawablesWithIntrinsicBounds(
                        R.mipmap.pills, 0, 0, 0);
                break;
            case("Syrup"):
                listItemText.setCompoundDrawablesWithIntrinsicBounds(
                        R.mipmap.syrup, 0, 0, 0);
                break;
            case("Eyedrops"):
                listItemText.setCompoundDrawablesWithIntrinsicBounds(
                        R.mipmap.eyedrops, 0, 0, 0);
                break;
            case("Injection"):
                listItemText.setCompoundDrawablesWithIntrinsicBounds(
                        R.mipmap.injection, 0, 0, 0);
                break;



        }

    }
    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        System.out.println("counter:"+counter);

        if(counter==0) {
            copyList.clear();
            copyList.addAll(list);
        }

        list.clear();


        if (charText.length() == 0) {
            list.addAll(copyList);
            counter=0;


        }
        else
        {

            for (Prescription p : copyList)
            {
                if (p.getPrescriptionName().toLowerCase(Locale.getDefault()).contains(charText)||
                        p.getPrescriptionType().toLowerCase(Locale.getDefault()).contains(charText) )
                {
                    list.add(p);
                    counter++;
                }
            }
        }
        notifyDataSetChanged();

    }

    private  class delPrescAsyncTask extends AsyncTask<String, Void, Boolean> {

        private User u;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(getContext(),
                    R.style.Theme_AppCompat_DayNight_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Deleting your prescription...");
            progressDialog.show();
            delete.setEnabled(false);
        }
        @Override
        protected Boolean doInBackground(String... prescription) {

            String pName= prescription[0];
            AppDatabase db= SigninActivity.getDB();
            u= MainActivity.signedInUser;
            System.out.println("del:"+pName);

            try {

                Notification.cancelNotification(db.dosetimeDao().getPrescriptionDoseTime(pName,u.getUserName()).get(0));
                db.prescriptionDao().deletePrescription(pName,u.getUserName());
                list.clear();
                list.addAll(db.prescriptionDao().getUserPrescription(u.getUserName()));
                notifyDataSetChanged();



                return true;
            }catch(Exception e){

            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean Result) {

            progressDialog.dismiss();
            delete.setEnabled(true);


        }


    }

}
