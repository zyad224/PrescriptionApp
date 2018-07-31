package com.example.zeyad.prescriptionapp.Database;

import android.content.SharedPreferences;

import com.example.zeyad.prescriptionapp.Acitvities.MainActivity;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Zeyad on 7/21/2018.
 *
 * This class is responsible to generate unique notification ids for prescriptions.
 *
 * When the app starts, the notification id starts by 1.
 *
 * When the user adds more and more prescriptions, the more notifications he will add.
 *
 * When creating a new notifcation and id is incremented and save in the shared pref.
 *
 * So the shared pref is holding the last notification id.
 *
 * No notification will have the same id >>>> Unique ids.
 *
 */

public class NotificationIDGenerator {


    private final static AtomicInteger c = new AtomicInteger(MainActivity.pref.getInt("id",1));  // you start somewhere
    public static int getID() {
        SharedPreferences.Editor editor = MainActivity.pref.edit();
        int newNotificationId=c.incrementAndGet();
        editor.putInt("id",newNotificationId);
        editor.apply();
        return newNotificationId;

    }
}
