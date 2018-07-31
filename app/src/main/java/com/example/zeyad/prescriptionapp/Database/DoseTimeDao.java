package com.example.zeyad.prescriptionapp.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Zeyad on 7/10/2018.
 *
 * This class provides the different sql queries to the entity DoseTime.
 */

@Dao
public interface DoseTimeDao {

    @Query("SELECT * FROM DoseTime WHERE user LIKE :username")
    public List<DoseTime> getAllDoseTimes (String username);

    @Query("SELECT * FROM DoseTime WHERE user LIKE :username AND prescription LIKE :prescripiton")
    public List<DoseTime> getPrescriptionDoseTime (String prescripiton,String username);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPrescriptionTime(DoseTime t);

    @Query ("UPDATE DoseTime SET dose_time1_eventid =:notiID1, dose_time2_eventid =:notiID2, " +
            "dose_time3_eventid =:notiID3,dose_time4_eventid =:notiID4, dose_time5_eventid =:notiID5 " +
            "WHERE user LIKE :username AND prescription LIKE :prescription ")
    void insertDoseNotificationIDs(int notiID1, int notiID2,int notiID3, int notiID4,int notiID5, String prescription, String username);

}
