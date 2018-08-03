package com.example.zeyad.prescriptionapp.Acitvities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
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

/**
 * This activity is responsible to show the user their doctor information in a form of a list.
 *
 * Users can also add new doctors information in this activity.
 */
public class MyDoctors extends AppCompatActivity {


    private ListView DoctorList;
    private ArrayList<String> docList;
    private TextView emptyList;
    private FloatingActionButton addDoc;


/////////////////////////MyDoctors Activity starts here/////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_doctors);

        mydoctorsGIU();
        new fetchMyDoctors().execute();

    }


/////////////////////////My Doctors Activity methods/////////////////////////////////////////////////////////

    /**
     * The method is responsible to set the GUI of MyDoctor Activity.
     * It sets:
     * 1- the doctor list view.
     * 2- the empty list text view.
     * 3- the add doc floating button to add new doctors.
     * 4- the adapter for the doctor list
     *
     *
     */
    private void mydoctorsGIU() {

        DoctorList = (ListView) findViewById(R.id.DoctorList);
        emptyList = (TextView) findViewById(R.id.emptyDoc);
        addDoc = (FloatingActionButton) findViewById(R.id.AdddocFab);

        docList = new ArrayList<String>();
        ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, docList);
        DoctorList.setAdapter(adapter);


        addDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation anim = android.view.animation.AnimationUtils.loadAnimation(addDoc.getContext(),R.anim.shake2);
                anim.setDuration(200L);
                addDoc.startAnimation(anim);
                addNewDoctors();

            }
        });
    }


    /**
     * This method is responsible to create a new intent to transfer the user to DoctorActivity
     * to add new doctors contacs and details.
     *
     */
    private void addNewDoctors(){
        Intent intent = new Intent(this, DoctorActivity.class);
        startActivity(intent);
        finish();
    }



//////////////////////////Asycn Tasks that are called on My Doctor Activity/////////////////////////////////////////

    /**
     * The async task is responsible to:
     * 1- fetch all doctors details from the db.
     * 2- show doctors details in a list form.
     * 3- if the doctors list is empty (ie no doctors found), notify the user to add new doctors.
     */
    private class fetchMyDoctors extends AsyncTask<Void, Void, Boolean> {

        private List<Doctor> docs;

        @Override
        /**
         * Checks if the credential exists or not.
         */
        protected Boolean doInBackground(Void... voids) {

            AppDatabase db = SigninActivity.getDB();
            docs = db.doctorDao().getAllDoctors(MainActivity.signedInUser.getUserName());
            System.out.println("docs size:"+ docs.size());

            for (Doctor t : docs) {

                if (!t.getDoctorName1().isEmpty() && !t.getDocnum1().isEmpty())
                    docList.add("Doctor:" + t.getDoctorName1() + " - "+" Telephone:" + t.getDocnum1());

                if (!t.getDoctorName2().isEmpty() && !t.getDocnum2().isEmpty())
                    docList.add("Doctor:" + t.getDoctorName2() + " - "+" Telephone:" + t.getDocnum2());

                if (!t.getDoctorName3().isEmpty() && !t.getDocnum3().isEmpty())
                    docList.add("Doctor:" + t.getDoctorName3() + " - "+" Telephone:" + t.getDocnum3());


            }


            return true;
        }


        protected void onPostExecute(Boolean credentialResult) {


            if (docList.isEmpty()) {
                DoctorList.setVisibility(View.INVISIBLE);
                emptyList.setVisibility(View.VISIBLE);
            }


        }
    }
}
