package com.example.zeyad.prescriptionapp.Database;

/**
 * Created by Zeyad on 7/5/2018.
 */

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;

@Database(entities = {Prescription.class, User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PrescriptionDao prescriptionDao();
    public abstract UserDao userDao();





    public static final Migration MIGRATION_0_1 = new Migration(0,1) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {



        }
    };
}
