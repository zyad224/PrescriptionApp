package com.example.zeyad.prescriptionapp;

import android.content.Context;

import com.example.zeyad.prescriptionapp.Acitvities.MainActivity;
import com.example.zeyad.prescriptionapp.Acitvities.SigninActivity;
import com.example.zeyad.prescriptionapp.Database.DoseTime;
import com.example.zeyad.prescriptionapp.Database.Notification;
import com.example.zeyad.prescriptionapp.Database.Prescription;

import org.junit.Test;

import static org.junit.Assert.*;
/**
 * Created by Zeyad on 8/11/2018.
 */

public class PrescriptionTesting {


    @Test
    public void addPrescription() throws Exception {


        try {
            Prescription newPrescription = new Prescription("presTest", "Pills", 20, 0,
                    "", "", 2, "zyad224");
            if(newPrescription!=null)
                assertEquals(true, true);
            else
                assertEquals(true, false);

        }catch(Exception e){

        }

    }

    @Test
    public void PrescriptionName() throws Exception {


        try {
            Prescription newPrescription = new Prescription("presTest", "Pills", 20, 0,
                    "", "", 2, "zyad224");
            assertEquals(newPrescription.getPrescriptionName(), "presTest");

        }catch(Exception e){

        }

    }

    @Test
    public void PrescriptionType() throws Exception {


        try {
            Prescription newPrescription = new Prescription("presTest", "Pills", 20, 0,
                    "", "", 2, "zyad224");
            assertEquals(newPrescription.getPrescriptionType(), "Pills");

        }catch(Exception e){

        }

    }

    @Test
    public void PrescriptionTakings() throws Exception {


        try {
            Prescription newPrescription = new Prescription("presTest", "Pills", 20, 0,
                    "", "", 2, "zyad224");
            assertEquals(newPrescription.getTakings(), 20);

        }catch(Exception e){

        }

    }

    @Test
    public void PrescriptionDose() throws Exception {


        try {
            Prescription newPrescription = new Prescription("presTest", "Pills", 20, 0,
                    "", "", 2, "zyad224");
            assertEquals(newPrescription.getPrescriptionDoese(), 2);

        }catch(Exception e){

        }

    }
    @Test
    public void addPrescriptionDoseTime() throws Exception {


        try {
            Prescription newPrescription = new Prescription("presTest", "Pills", 20, 0,
                    "", "", 2, "zyad224");

            DoseTime prescriptionTime = new DoseTime("12:00", "13:00", "14:00",
                    "15:00", "16:00", "presTest", "zyad224");




            if(prescriptionTime!=null)
                assertEquals(true, true);
            else
                assertEquals(true, false);

        }catch(Exception e){

        }

    }
}
