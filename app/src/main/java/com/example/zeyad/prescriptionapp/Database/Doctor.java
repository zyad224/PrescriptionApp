package com.example.zeyad.prescriptionapp.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Zeyad on 7/31/2018.
 */

@Entity
public class Doctor {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name="doc1")
    public String doctorName1;

    @ColumnInfo(name="doc2")
    public String doctorName2;

    @ColumnInfo(name="doc3")
    public String doctorName3;

    @ColumnInfo(name="num1")
    public String docnum1;

    @ColumnInfo(name="num2")
    public String docnum2;

    @ColumnInfo(name="num3")
    public String docnum3;

    @ColumnInfo(name="user")
    public String user;

    public Doctor(String doctorName1,String doctorName2, String doctorName3, String docnum1,String docnum2,String docnum3, String user){
        this.doctorName1=doctorName1;
        this.doctorName2=doctorName2;
        this.doctorName3=doctorName3;
        this.docnum1=docnum1;
        this.docnum2=docnum2;
        this.docnum3=docnum3;
        this.user=user;
    }

    public String getDoctorName1(){
        return this.doctorName1;
    }

    public String getDoctorName2(){
        return this.doctorName2;
    }
    public String getDoctorName3(){
        return this.doctorName3;
    }
    public String getDocnum1(){
        return this.docnum1;
    }
    public String getDocnum2(){
        return this.docnum2;
    }
    public String getDocnum3(){
        return this.docnum3;
    }
    public String getUser(){
        return this.user;
    }
}
