package com.example.zeyad.prescriptionapp.Database;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.zeyad.prescriptionapp.Services.NotificationService;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Zeyad on 7/18/2018.
 */

@Entity
public class Notification {

    private AlarmManager alarmManager;
    private int notificationID;
    private DoseTime prescriptionTime;
    private static int idCounter =1;
    private Context ctx;
    private PendingIntent pending;
    private String [] doseTimings =new String [5];






    public Notification(DoseTime prescriptionTime, Context ctx){

        this.prescriptionTime=prescriptionTime;
        this.notificationID=idCounter++;
        this.ctx=ctx;

        doseTimings[0]=this.prescriptionTime.getDoseTime1();
        doseTimings[1]=this.prescriptionTime.getDoseTime2();
        doseTimings[2]=this.prescriptionTime.getDoseTime3();
        doseTimings[3]=this.prescriptionTime.getDoseTime4();
        doseTimings[4]=this.prescriptionTime.getDoseTime5();
        setNotification();


    }

    private void setNotification(){
        Intent intent= new Intent (this.ctx, NotificationService.class);



        intent.putExtra("prescription name",this.prescriptionTime.getPrescription_name());
        intent.putExtra("user",this.prescriptionTime.getUser());


        alarmManager= (AlarmManager)ctx.getSystemService(ALARM_SERVICE);




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


                System.out.println(calendar.get(Calendar.HOUR));
                System.out.println(calendar.get(Calendar.MINUTE));
                System.out.println(calendar.get(Calendar.AM_PM));

                int notificationId= NotificationIDGenerator.getID();
                pending = PendingIntent.getBroadcast(this.ctx,notificationId,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                System.out.println("noti id:"+notificationId);


                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pending);
                Log.d("0","setRepeating");

            }

        }




    }






}
