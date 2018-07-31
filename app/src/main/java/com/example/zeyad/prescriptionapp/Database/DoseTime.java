package com.example.zeyad.prescriptionapp.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Zeyad on 7/9/2018.
 *
 * This class is describing the DoseTime entity.
 *
 * Every Dose Time object has:
 * 1- 5 dose times.
 * 2- 5 dose times unique notification ids.
 * 3- unique prescription name.
 * 4- unique user id.
 */

@Entity(foreignKeys = {
        @ForeignKey(entity = Prescription.class,
                parentColumns = "prescription_name",
                childColumns = "prescription",
                onDelete=CASCADE),
        @ForeignKey(entity = User.class,
                parentColumns = "userName",
                childColumns = "user",
                onDelete=CASCADE)

})
public class DoseTime {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name="dose_time1")
    public String doseTime1;
    @ColumnInfo(name="dose_time1_eventid")
    public int doseTime1EventID;

    @ColumnInfo(name="dose_time2")
    public String doseTime2;
    @ColumnInfo(name="dose_time2_eventid")
    public int doseTime2EventID;

    @ColumnInfo(name="dose_time3")
    public String doseTime3;
    @ColumnInfo(name="dose_time3_eventid")
    public int doseTime3EventID;

    @ColumnInfo(name="dose_time4")
    public String doseTime4;
    @ColumnInfo(name="dose_time4_eventid")
    public int doseTime4EventID;

    @ColumnInfo(name="dose_time5")
    public String doseTime5;
    @ColumnInfo(name="dose_time5_eventid")
    public int doseTime5EventID;

    @ColumnInfo(name="prescription")
    public String prescription;
    @ColumnInfo(name="user")
    public String user;

    public DoseTime(String doseTime1,String doseTime2,String doseTime3,String doseTime4,String doseTime5,String prescription,String user){
        this.doseTime1=doseTime1;
        this.doseTime2=doseTime2;
        this.doseTime3=doseTime3;
        this.doseTime4=doseTime4;
        this.doseTime5=doseTime5;
        this.prescription=prescription;
        this.user=user;
    }

    public int getId(){return  this.id;}
    public String getDoseTime1(){return this.doseTime1;}
    public String getDoseTime2(){return this.doseTime2;}
    public String getDoseTime3(){return this.doseTime3;}
    public String getDoseTime4(){return this.doseTime4;}
    public String getDoseTime5(){return this.doseTime5;}
    public String getPrescription_name(){return this.prescription;}
    public String getUser(){return this.user;}
    public int getDoseTime1EventID(){return this.doseTime1EventID;}
    public int getDoseTime2EventID(){return this.doseTime2EventID;}
    public int getDoseTime3EventID(){return this.doseTime3EventID;}
    public int getDoseTime4EventID(){return this.doseTime4EventID;}
    public int getDoseTime5EventID(){return this.doseTime5EventID;}





}
