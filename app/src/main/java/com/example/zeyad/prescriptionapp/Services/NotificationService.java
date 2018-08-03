package com.example.zeyad.prescriptionapp.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.zeyad.prescriptionapp.R;
import com.example.zeyad.prescriptionapp.Acitvities.SigninActivity;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

/**
 * This class is the Notification Service that responsible to show notification to the user to take thier prescriptions.
 *
 * 1- It works as a BroadcastReceiver, it recieves the prescription from an intent.
 * 2- It uses the NotificationManager to create a notification channel for notification (required for Anroid +8).
 * 3- It uses NotificationCompat to build the notification.
 * 4- If notification is clicked outside the app, the app directs the user ot signIn activity.
 * 6- It uses unique ids for notifications.
 *
 */
public class NotificationService extends BroadcastReceiver {


    private static final int NOTIFICATION_ID = 1;
    public static final String NOTIFICATION_CHANNEL_ID = "my_notification_channel";
    private static  NotificationManager nM;

    public NotificationService() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("0","in service");


        String prescriptionName = intent.getExtras().getString("pname");
        String prescriptionType = intent.getExtras().getString("ptype");
        String prescriptionDose = intent.getExtras().getString("pdose");
        String prescriptionUser = intent.getExtras().getString("puser");







        nM = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
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

        Intent repeatingintent = new Intent(context, SigninActivity.class);
        repeatingintent.putExtra("pname",prescriptionName);
        repeatingintent.putExtra("puser",prescriptionUser);
        repeatingintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent intentToStart = PendingIntent.getActivity(context, NOTIFICATION_ID, repeatingintent, FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setVibrate(new long[]{0, 100, 100, 100, 100, 100})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Take your Prescription"+ ","+ prescriptionUser)
                .setContentText(prescriptionName +"-" + prescriptionType+ " , " + prescriptionDose + " Dose")
                .setContentIntent(intentToStart)
                .setAutoCancel(true);



        nM.notify(NOTIFICATION_ID, builder.build());

    }




}
