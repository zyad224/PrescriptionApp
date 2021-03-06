package com.example.zeyad.prescriptionapp.Adapters;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.zeyad.prescriptionapp.Database.AppDatabase;
import com.example.zeyad.prescriptionapp.Database.DoseTime;
import com.example.zeyad.prescriptionapp.Database.Prescription;
import com.example.zeyad.prescriptionapp.Database.User;
import com.example.zeyad.prescriptionapp.Acitvities.MainActivity;
import com.example.zeyad.prescriptionapp.R;
import com.example.zeyad.prescriptionapp.Services.NotificationService;
import com.example.zeyad.prescriptionapp.Acitvities.SigninActivity;

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
    private Context ctx;

    public static int counter=0;
    public ListAdapterPresLog(@NonNull Context context, int resource, List<Prescription> textViewResourceId) {
        super(context, resource, textViewResourceId);
        list=textViewResourceId;
        this.copyList=new ArrayList<Prescription>();
        ctx=context;

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

                cancelNotification(db.dosetimeDao().getPrescriptionDoseTime(pName,u.getUserName()).get(0));
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

            System.out.print(Result);
            progressDialog.dismiss();
            delete.setEnabled(true);


        }


    }


    private void cancelNotification(DoseTime dt){

        System.out.println("in cancel:");

        Intent intent = new Intent(ctx, NotificationService.class);
        ArrayList<Integer> cancelledNotificationIds=new ArrayList<Integer>();
        System.out.println("in cancel:");

        cancelledNotificationIds.add(dt.getDoseTime1EventID());
        cancelledNotificationIds.add(dt.getDoseTime2EventID());
        cancelledNotificationIds.add(dt.getDoseTime3EventID());
        cancelledNotificationIds.add(dt.getDoseTime4EventID());
        cancelledNotificationIds.add(dt.getDoseTime5EventID());

        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);

        System.out.println("in cancel:"+cancelledNotificationIds.size());


        for(int i=0;i<cancelledNotificationIds.size();i++) {
            System.out.println(cancelledNotificationIds.get(i));


            if(cancelledNotificationIds.get(i)!=0) {
                PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, cancelledNotificationIds.get(i), intent, 0);
                am.cancel(pendingIntent);
                pendingIntent.cancel();
                System.out.println("notification cancelled no:"+ cancelledNotificationIds.get(i));
            }
        }


    }

}
