package com.example.zeyad.prescriptionapp.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

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



    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "prescription_name")
    public String prescriptionName;

    @ColumnInfo(name="prescription_type")
    public String prescriptionType;

    @ColumnInfo(name="number_takings")
    public int numberTakings;

    @ColumnInfo(name="doctor_name")
    public String doctorName;

    @ColumnInfo(name="doctor_number")
    public String doctorNumber;

    @ColumnInfo(name="prescription_dose")
    public int prescriptionDose;

    @ColumnInfo(name="dose_time1")
    public String time1;
    @ColumnInfo(name="dose_time2")
    public String time2;
    @ColumnInfo(name="dose_time3")
    public String time3;
    @ColumnInfo(name="dose_time4")
    public String time4;
    @ColumnInfo(name="dose_time5")
    public String time5;

    @ColumnInfo(name="user_id")
    public String user_id;


    public Prescription(String prescriptionName,String prescriptionType,int numberTakings,String doctorName,
                        String doctorNumber, int prescriptionDose,String time1,String time2,
            String time3,String time4,String time5, String user_id){

        this.prescriptionName=prescriptionName;
        this.prescriptionType=prescriptionType;
        this.numberTakings=numberTakings;
        this.doctorName=doctorName;
        this.doctorNumber=doctorNumber;
        this.prescriptionDose=prescriptionDose;
        this.time1=time1;
        this.time2=time2;
        this.time3=time3;
        this.time4=time4;
        this.time5=time5;
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

    public String getDoseTime1(){
        return  this.time1;
    }
    public String getDoseTime2(){
        return  this.time2;
    }
    public String getDoseTime3(){
        return  this.time3;
    }
    public String getDoseTime4(){
        return  this.time4;
    }
    public String getDoseTime5(){
        return  this.time5;
    }
    public String getUser_id(){
        return this.user_id;
    }
    public int getId(){
        return this.id;
    }



}
