package com.example.zeyad.prescriptionapp.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zeyad on 7/5/2018.
 */

@Dao
public interface PrescriptionDao {

//    @Query("SELECT * FROM Prescription")
//    public List<Prescription> getAllPrescriptions ();

    @Query("SELECT * FROM Prescription WHERE user_id LIKE :username")
    public List<Prescription> getUserPrescription (String username);

    @Query("SELECT * FROM Prescription WHERE user_id LIKE :username AND prescription_name LIKE :pname ")
    public Prescription getSpecificPrescription (String pname,String username);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPrescription(Prescription p);

    @Query ("Delete From Prescription WHERE user_id LIKE :username AND prescription_name LIKE :pname ")
    void deletePrescription(String pname, String username);

    @Query ("UPDATE Prescription SET forget_takings =:reducedTakings WHERE user_id LIKE :username AND prescription_name LIKE :pname ")
    void updatePrescriptionTakings(int reducedTakings, String pname, String username);
}
