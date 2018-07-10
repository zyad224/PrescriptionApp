package com.example.zeyad.prescriptionapp.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Zeyad on 7/10/2018.
 */

@Dao
public interface DoseTimeDao {

    @Query("SELECT * FROM DoseTime WHERE user LIKE :username")
    public List<DoseTime> getAllDoseTimes (String username);

    @Query("SELECT * FROM DoseTime WHERE user LIKE :username AND prescription LIKE :prescripiton")
    public List<DoseTime> getPrescriptionDoseTime (String prescripiton,String username);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPrescriptionTime(DoseTime t);
}
