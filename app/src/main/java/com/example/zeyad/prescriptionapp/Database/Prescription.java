package com.example.zeyad.prescriptionapp.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Zeyad on 7/5/2018.
 */

@Entity(foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "userName",
        childColumns = "user_id",
        onDelete=CASCADE))
public class Prescription {



//    @PrimaryKey(autoGenerate = true)
//    public int id;

    @PrimaryKey
    @ColumnInfo(name="prescription_name")
    @NonNull public String prescriptionName;


    @ColumnInfo(name="prescription_type")
    public String prescriptionType;

    @ColumnInfo(name="number_takings")
    public int numberTakings;

    @ColumnInfo(name="forget_takings")
    public int forgetTakings;

    @ColumnInfo(name="doctor_name")
    public String doctorName;

    @ColumnInfo(name="doctor_number")
    public String doctorNumber;

    @ColumnInfo(name="prescription_dose")
    public int prescriptionDose;

    @ColumnInfo(name="dose_time")
    public String doseTime;


    @ColumnInfo(name="user_id")
    public String user_id;





    public Prescription(String prescriptionName,String prescriptionType,int numberTakings,int forgetTakings,String doctorName,
                        String doctorNumber, int prescriptionDose, String user_id){
        this.prescriptionName=prescriptionName;
        this.prescriptionType=prescriptionType;
        this.numberTakings=numberTakings;
        this.forgetTakings=forgetTakings;
        this.doctorName=doctorName;
        this.doctorNumber=doctorNumber;
        this.prescriptionDose=prescriptionDose;
        this.user_id=user_id;

    }
    @Ignore
    public Prescription(String prescriptionName,String prescriptionType,int numberTakings,String doctorName,
                        String doctorNumber, int prescriptionDose,String doseTime, String user_id){

        this.prescriptionName=prescriptionName;
        this.prescriptionType=prescriptionType;
        this.numberTakings=numberTakings;
        this.doctorName=doctorName;
        this.doctorNumber=doctorNumber;
        this.prescriptionDose=prescriptionDose;
        this.doseTime=doseTime;
        this.user_id=user_id;

    }

    public String getPrescriptionName(){
        return this.prescriptionName;
    }
    public String getPrescriptionType(){
        return this.prescriptionType;
    }
    public int getTakings(){
        return this.numberTakings;
    }
    public String getDoctorName(){
        return this.doctorName;
    }
    public String getDoctorNumber(){
        return this.doctorNumber;
    }
    public int getPrescriptionDoese(){
        return this.prescriptionDose;
    }
    public String getDoseTime(){
        return  this.doseTime;
    }
    public int getForgetTakings(){return  this.forgetTakings;}
    public String getUser_id(){
        return this.user_id;
    }




}
