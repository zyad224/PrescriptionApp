package com.example.zeyad.prescriptionapp.Acitvities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zeyad.prescriptionapp.Database.AES;
import com.example.zeyad.prescriptionapp.Database.AppDatabase;
import com.example.zeyad.prescriptionapp.Database.Doctor;
import com.example.zeyad.prescriptionapp.Database.User;
import com.example.zeyad.prescriptionapp.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyDoctors extends AppCompatActivity {


    private ListView DoctorList;
    private ArrayList<String> docList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_doctors);

        mydoctorsGIU();
        new fetchMyDoctors().execute();

    }

    private void mydoctorsGIU(){

        DoctorList=(ListView) findViewById(R.id.DoctorList);
        docList=new ArrayList<String>();
        ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, docList);
        DoctorList.setAdapter(adapter);
    }


    private  class fetchMyDoctors extends AsyncTask<Void, Void, Boolean> {

        private List<Doctor> docs;

        @Override
        /**
         * Checks if the credential exists or not.
         */
        protected Boolean doInBackground(Void... voids) {

            AppDatabase db= SigninActivity.getDB();
            docs=db.doctorDao().getAllDoctors(MainActivity.signedInUser.getUserName());

            for(Doctor t: docs){

                if(!t.getDoctorName1().isEmpty()&& !t.getDocnum1().isEmpty())
                    docList.add("Doctor:"+t.getDoctorName1() + " Number:"+ t.getDocnum1());

                else if(!t.getDoctorName1().isEmpty()&& !t.getDocnum1().isEmpty())
                    docList.add("Doctor:"+t.getDoctorName2() + " Number:"+ t.getDocnum2());

                else if(!t.getDoctorName1().isEmpty()&& !t.getDocnum1().isEmpty())
                    docList.add("Doctor:"+t.getDoctorName3() + " Number:"+ t.getDocnum3());


            }



            return true;
        }




    }
}
