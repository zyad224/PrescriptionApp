package com.example.zeyad.prescriptionapp.Database;

/**
 * Created by Zeyad on 7/5/2018.
 *
 * This class is responsible to initilize the database of the mob app.
 *
 * It works as an a gateway to the interface of different DAOs:
 * 1- PrescriptionDao
 * 2- UserDao
 * 3- DoseTimeDao
 */

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;

@Database(entities = {Prescription.class, User.class,DoseTime.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PrescriptionDao prescriptionDao();
    public abstract UserDao userDao();
    public abstract DoseTimeDao dosetimeDao();






    public static final Migration MIGRATION_0_1 = new Migration(0,1) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {



        }
    };
}
