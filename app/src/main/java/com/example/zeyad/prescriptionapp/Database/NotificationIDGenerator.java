package com.example.zeyad.prescriptionapp.Database;

import android.content.SharedPreferences;

import com.example.zeyad.prescriptionapp.Acitvities.MainActivity;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Zeyad on 7/21/2018.
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
