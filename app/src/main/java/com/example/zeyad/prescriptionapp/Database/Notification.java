package com.example.zeyad.prescriptionapp.Database;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.zeyad.prescriptionapp.R;
import com.example.zeyad.prescriptionapp.Services.NotificationService;
import com.example.zeyad.prescriptionapp.Acitvities.SigninActivity;

import java.util.ArrayList;
import java.util.Calendar;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.content.Context.ALARM_SERVICE;
import static com.example.zeyad.prescriptionapp.Services.NotificationService.NOTIFICATION_CHANNEL_ID;


/**
 * Created by Zeyad on 7/18/2018.
 *
 * This is the Notification Class. it is responsible to:
 *
 * 1- create  unique notification ids for each dose time of each prescription.
 * 2- create the notification by calling the notification service.
 * 3- saving notification ids in the db.
 * 4- cancel notifications.
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


    /**
     * This is the constructor of the notification class.
     * It recieves the precription and list of prescription times and context.
     * @param prescriptionTime
     * @param prescription
     * @param ctx
     */
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

    public Notification(Prescription prescription, Context ctx){

        this.prescription=prescription;
        this.ctx=ctx;

        refillNotification();

    }

    private void refillNotification(){


        int notificationId= NotificationIDGenerator.getID();

       NotificationManager nM = (NotificationManager) this.ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            nM.createNotificationChannel(notificationChannel);
        }

        Intent intent = new Intent(this.ctx, SigninActivity.class);
        intent.putExtra("pname",this.prescription.getPrescriptionName());
        intent.putExtra("puser",this.prescription.getUser_id());
        intent.putExtra("refill","1");

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent intentToStart = PendingIntent.getActivity(this.ctx, notificationId, intent, FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.ctx, NOTIFICATION_CHANNEL_ID)
                .setVibrate(new long[]{0, 100, 100, 100, 100, 100})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Refill your Prescription now"+ ","+ this.prescription.getUser_id())
                .setContentText(this.prescription.getPrescriptionName())
                .setContentIntent(intentToStart)
                .setAutoCancel(true);


        nM.notify(notificationId, builder.build());

    }

    /**
     * The method responsible to generate notifications for every prescription time.
     * It uses alarm manager to call the notification service.
     * It also use the method generateNotificationIDs to generate unique notification ids
     * then save them in the db.
     *
     */
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


    /**
     * The method is responsible to generate unique notification ids by using the
     * NotificationIDGenerator which use shared preference to save the last notification id on the app.
     * @param intent
     */
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

    /**
     * The method deletes the notification of each dose time of a prescription.
     * It fetches the unique notification ids from db then create and pending intent and canecl it.
     * @param dt
     */
    public static void cancelNotification(DoseTime dt){


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
            }
        }


    }

    /**
     * The async task is responsible to:
     * To save notification ids for the 5 dose times for every prescription added tot he app.
     */
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
