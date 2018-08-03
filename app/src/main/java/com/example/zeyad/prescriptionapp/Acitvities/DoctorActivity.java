package com.example.zeyad.prescriptionapp.Acitvities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zeyad.prescriptionapp.Database.AES;
import com.example.zeyad.prescriptionapp.Database.AppDatabase;
import com.example.zeyad.prescriptionapp.Database.Doctor;
import com.example.zeyad.prescriptionapp.Database.User;
import com.example.zeyad.prescriptionapp.R;

/**
 * This class is responsible to show the DoctorActivity to the user.
 *
 * The DoctorActivity allows the users to add their doctors contacts to the app.
 *
 * This activity launches only when the app starts for the first time.
 *
 * The user might also add their doctors contacts from My Doctor List from the menue.
 */
public class DoctorActivity extends AppCompatActivity {

    private EditText doctor1,doctor2,doctor3;
    private EditText number1,number2,number3;
    private FloatingActionButton addDoctor;
    private ImageView img;
    private String doc1,doc2,doc3;
    private String num1,num2,num3;



 /////////////////////////Doctor Activity starts here/////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        doctorGUI();
    }


/////////////////////////Doctor Activity methods/////////////////////////////////////////////////////////

    /**
     * The method is responsible to set the GUI of Doctor Activity.
     * It sets:
     * 1- the edit boxes.
     * 2- the image of doctor.
     * 3- the add doctor floating button.
     *
     *
     */
    private void doctorGUI(){
        doctor1=(EditText) findViewById(R.id.doctor1);
        doctor2=(EditText) findViewById(R.id.doctor2);
        doctor3=(EditText) findViewById(R.id.doctor3);


        number1=(EditText) findViewById(R.id.number1);
        number2=(EditText) findViewById(R.id.number2);
        number3=(EditText) findViewById(R.id.number3);

        img=(ImageView) findViewById(R.id.doctorLogo);
        addDoctor= (FloatingActionButton) findViewById(R.id.addDoc);


        addDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("in z0");

                // checks username and password
                if(validateUserinput()){
                    System.out.println("in z1");
                    String[] doctors =new String [6] ;
                    doctors[0]=doc1;
                    doctors[1]=doc2;
                    doctors[2]=doc3;
                    doctors[3]=num1;
                    doctors[4]=num2;
                    doctors[5]=num3;
                    new addDoctorToDB().execute(doctors);
                }

            }
        });


    }


    /**
     * The method check if the user has input at least one doctor contacts.
     * @return
     */
    private boolean validateUserinput(){
        doc1=doctor1.getText().toString();
        doc2=doctor2.getText().toString();
        doc3=doctor3.getText().toString();

        num1=number1.getText().toString();
        num2=number2.getText().toString();
        num3=number3.getText().toString();




        if((!doc1.isEmpty()&&!num1.isEmpty())||(!doc2.isEmpty()&&!num2.isEmpty())||(!doc3.isEmpty()&&!num3.isEmpty()))
        {
            System.out.println("in z2");

            return true;
        }
        else {

            Snackbar.make(addDoctor,
                        "Please Add at least one doctor details", Snackbar.LENGTH_LONG).show();
        }

        return false;
    }


//////////////////////////Asycn Tasks that are called on Doctor Activity/////////////////////////////////////////

    /**
     * The async task is responsible to:
     * 1- fetch doctor details from form.
     * 2- add doctor details to the db.
     * 3- notify the user that they added their doctor details.
     */
    private  class addDoctorToDB extends AsyncTask<String[], Void, Boolean> {

        private boolean flag;

        @Override

        protected Boolean doInBackground(String[]... doctors) {

            AppDatabase db= SigninActivity.getDB();
            String doc []= doctors[0];

            try {
                Doctor d = new Doctor(doc[0], doc[1], doc[2], doc[3], doc[4], doc[5], MainActivity.signedInUser.getUserName());
                db.doctorDao().insertDoctors(d);
                flag=true;
            }catch (Exception e){

                flag=false;

            }

            return flag;
        }

        @Override

        protected void onPostExecute(Boolean Result) {

            if(Result) {
                Snackbar.make(addDoctor,
                        "You added Doctors Details!", Snackbar.LENGTH_LONG).show();

                getSharedPreferences("MyPrefs", MODE_PRIVATE).edit()
                        .putBoolean("firstRun8", false).apply();

                finish();
            }

        }


    }
}
