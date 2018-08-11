package com.example.zeyad.prescriptionapp;

import com.example.zeyad.prescriptionapp.Acitvities.MainActivity;
import com.example.zeyad.prescriptionapp.Database.Doctor;
import com.example.zeyad.prescriptionapp.Database.Prescription;

import org.junit.Test;

import static org.junit.Assert.*;
/**
 * Created by Zeyad on 8/11/2018.
 */

public class DoctorTesting {


    @Test
    public void addDoctorDetails() throws Exception {


        try {

            Doctor d = new Doctor("doc1", "doc2", "doc3", "num1"
                    , "num2", "num3", "zyad224");

            if(d!=null)
                assertEquals(true, true);
            else
                assertEquals(true, false);

        }catch(Exception e){

        }

    }

    @Test
    public void Doc1Details() throws Exception {


        try {

            Doctor d = new Doctor("doc1", "doc2", "doc3", "num1"
                    , "num2", "num3", "zyad224");


            assertEquals(d.getDoctorName1(), "doc1");
            assertEquals(d.getDocnum1(), "num1");


        }catch(Exception e){

        }

    }

    @Test
    public void Doc2Details() throws Exception {


        try {

            Doctor d = new Doctor("doc1", "doc2", "doc3", "num1"
                    , "num2", "num3", "zyad224");


            assertEquals(d.getDoctorName2(), "doc2");
            assertEquals(d.getDocnum2(), "num2");


        }catch(Exception e){

        }

    }

    @Test
    public void Doc3Details() throws Exception {


        try {

            Doctor d = new Doctor("doc1", "doc2", "doc3", "num1"
                    , "num2", "num3", "zyad224");


            assertEquals(d.getDoctorName3(), "doc3");
            assertEquals(d.getDocnum3(), "num3");


        }catch(Exception e){

        }

    }
}
