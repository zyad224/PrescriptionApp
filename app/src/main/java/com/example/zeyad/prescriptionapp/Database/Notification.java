package com.example.zeyad.prescriptionapp.Database;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.zeyad.prescriptionapp.Services.NotificationService;
import com.example.zeyad.prescriptionapp.SigninActivity;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.SYSTEM_HEALTH_SERVICE;

/**
 * Created by Zeyad on 7/18/2018.
 */


public class Notification {

    private int maximumDose=5;
    private AlarmManager alarmManager;
    private DoseTime prescriptionTime;
    private Prescription prescription;
    private  static Context ctx;
    private PendingIntent pending;
    private String [] doseTimings =new String [maximumDose];
    private ArrayList<Integer> doseNotificationId=new ArrayList<Integer>();
    private Prescription prescriptionToShowInNotfi;






    public Notification(DoseTime prescriptionTime,Prescription prescription, Context ctx){

        this.prescription=prescription;
        this.prescriptionTime=prescriptionTime;
        this.ctx=ctx;

        doseTimings[0]=this.prescriptionTime.getDoseTime1();
        doseTimings[1]=this.prescriptionTime.getDoseTime2();
        doseTimings[2]=this.prescriptionTime.getDoseTime3();
        doseTimings[3]=this.prescriptionTime.getDoseTime4();
        doseTimings[4]=this.prescriptionTime.getDoseTime5();
        generateNotification();


    }

    private void generateNotification(){
         Intent intent= new Intent (this.ctx, NotificationService.class);

         intent.putExtra("pname",this.prescription.getPrescriptionName());
         intent.putExtra("ptype", this.prescription.getPrescriptionType());
         intent.putExtra("pdose",Integer.toString(this.prescription.getPrescriptionDoese()));
         intent.putExtra("puser",this.prescription.getUser_id());

         alarmManager= (AlarmManager)ctx.getSystemService(ALARM_SERVICE);

         generateNotificationIDs(intent);

         new saveDoseNotificationIDs().execute();



    }


    private void generateNotificationIDs( Intent intent){

        for(int i=0;i<doseTimings.length;i++){
            if(!doseTimings[i].isEmpty()){


                String Timings[]=doseTimings[i].split(":");




                Calendar calendar=Calendar.getInstance();
                calendar.set(Calendar.HOUR,Integer.parseInt(Timings[0]));
                calendar.set(Calendar.MINUTE,Integer.parseInt(Timings[1]));

                if(Timings[2].equalsIgnoreCase("AM"))
                    calendar.set(Calendar.AM_PM,Calendar.AM);
                else
                    calendar.set(Calendar.AM_PM,Calendar.PM);




                int notificationId= NotificationIDGenerator.getID();

                pending = PendingIntent.getBroadcast(this.ctx,notificationId,intent,PendingIntent.FLAG_UPDATE_CURRENT);

                doseNotificationId.add(notificationId);

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pending);

            }

        }

    }

    public static void cancelNotification(DoseTime dt){

        System.out.println("in cancel");

        Intent intent = new Intent(ctx, NotificationService.class);
        ArrayList<Integer> cancelledNotificationIds=new ArrayList<Integer>();

        cancelledNotificationIds.add(dt.getDoseTime1EventID());
        cancelledNotificationIds.add(dt.getDoseTime2EventID());
        cancelledNotificationIds.add(dt.getDoseTime3EventID());
        cancelledNotificationIds.add(dt.getDoseTime4EventID());
        cancelledNotificationIds.add(dt.getDoseTime5EventID());

        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);


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
    private  class saveDoseNotificationIDs extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected void onPreExecute() {


        }
        @Override
        protected Boolean doInBackground(Void... voids) {

            System.out.println("this:"+prescriptionTime.getUser() + " "+ prescriptionTime.getPrescription_name());
            System.out.println("this2:"+doseNotificationId.size());

            AppDatabase db= SigninActivity.getDB();
            switch(doseNotificationId.size()){
                case 1:
                    db.dosetimeDao().insertDoseNotificationIDs(doseNotificationId.get(0),0,0,0,0
                            ,prescriptionTime.getPrescription_name(),prescriptionTime.getUser());
                    break;
                case 2:
                    db.dosetimeDao().insertDoseNotificationIDs(doseNotificationId.get(0),doseNotificationId.get(1),0,0,0
                            ,prescriptionTime.getPrescription_name(),prescriptionTime.getUser());
                    break;
                case 3:
                    db.dosetimeDao().insertDoseNotificationIDs(doseNotificationId.get(0),doseNotificationId.get(1),doseNotificationId.get(2),0,0
                            ,prescriptionTime.getPrescription_name(),prescriptionTime.getUser());
                    break;
                case 4:
                    db.dosetimeDao().insertDoseNotificationIDs(doseNotificationId.get(0),doseNotificationId.get(1),doseNotificationId.get(2),doseNotificationId.get(3),0
                            ,prescriptionTime.getPrescription_name(),prescriptionTime.getUser());
                    break;
                case 5:
                    db.dosetimeDao().insertDoseNotificationIDs(doseNotificationId.get(0),doseNotificationId.get(1),doseNotificationId.get(2),doseNotificationId.get(3),doseNotificationId.get(4)
                            ,prescriptionTime.getPrescription_name(),prescriptionTime.getUser());
                    break;
            }





            return true;
        }


        @Override
        protected void onPostExecute(Boolean insertingResult) {



        }


    }












}
