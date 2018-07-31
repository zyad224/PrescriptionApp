package com.example.zeyad.prescriptionapp.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Zeyad on 7/31/2018.
 */

@Dao
public interface DoctorDao {


    @Query("SELECT * FROM Doctor WHERE user LIKE :username")
    public List<Doctor> getAllDoctors (String username);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDoctors(Doctor d);
}
