package com.example.zeyad.prescriptionapp.Acitvities;

import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.zeyad.prescriptionapp.Database.AES;
import com.example.zeyad.prescriptionapp.Database.AppDatabase;
import com.example.zeyad.prescriptionapp.Database.User;
import com.example.zeyad.prescriptionapp.R;


/**
 * This is the sign in activity of the the mobile app.
 * It initializes the interface of the sign in activity .
 * It checks user input username, password, etc.
 * It directs the user either to the main activity or to the sign up activity.
 * It initialises the db of the app.
 *
 */
public class SigninActivity extends AppCompatActivity {

    private EditText userName;
    private EditText password;
    private Button login;
    private TextView tv;
    private ImageView img;
    private String UserName,Password;
    private ProgressDialog progressDialog;

    public static AppDatabase db;





/////////////////////////////SignIn Activity starts here/////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        signinGUI();

    }





/////////////////////////////////SignIn Activity methods/////////////////////////////////////////////////////////////////

    /**
     *
     * The method is responsible to set the GUI of the sign in activity of the app.
     * It sets:
     * 1- the username edit text.
     * 2- the password edit text.
     * 3- the login button.
     * 4- the create account textview ( the way to login activity).
     * 5- the icon of the app.
     * 6- it initialises the db object.
     * 7- set click listeners on the login button and the create account text view.
     * 8- checks if the credentials of the user is stored in the db or not ( username password).
     *
     */
    private void signinGUI(){


        // set different attributes
        userName=(EditText) findViewById(R.id.input_username_signin);
        password=(EditText) findViewById(R.id.input_password_signin);
        login=(Button) findViewById(R.id.login);
        tv=(TextView) findViewById(R.id.signup_link);
        img=(ImageView) findViewById(R.id.appLogo);

        //set the color of the login button and size of app icon
        int red= Color.rgb(196,3,3);
        login.setBackgroundColor(red);
        login.setTextColor(Color.WHITE);
        resizeAppIcon(img);

        // set the db object.
        if (db==null) {
            db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "app_db2")
                    .fallbackToDestructiveMigration()
                    .build();
        }


        // set click listener on login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // checks username and password
                if(validateUserinput()){
                    String[] credentials =new String [2] ;
                    credentials[0]=UserName;
                    credentials[1]=Password;
                    new signInAsyncTask().execute(credentials);
                }

            }
        });

        // set click listener on create account text view
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // go to the sigup activity
                Intent intent = new Intent( getApplicationContext(),SignupActivity.class);
                startActivity(intent);
            }
        });

    }


    /**
     * The method checks if the user has entered his username and password or not.
     * If the user forgets to enter any of them, this method notifies the user to re-enter their credentials.
     * @return
     */
    private boolean validateUserinput(){
        UserName=userName.getText().toString();
        Password=password.getText().toString();

        if(!UserName.isEmpty()&&!Password.isEmpty()) {
            return true;
        }
        else {

             if (UserName.isEmpty())
                Snackbar.make(findViewById(android.R.id.content),
                        "Please Add your username", Snackbar.LENGTH_LONG).show();

            else if (Password.isEmpty())
                Snackbar.make(findViewById(android.R.id.content),
                        "Please Add your Password", Snackbar.LENGTH_LONG).show();
        }
        return false;
    }

    /**
     * The method is responsible to resize the app icon 400x400 pixels
     * @param img
     */
    public void resizeAppIcon(ImageView img){
        int newHeight = 400;
        int newWidth = 400;
        img.requestLayout();
        img.getLayoutParams().height = newHeight;

        img.getLayoutParams().width = newWidth;

        img.setScaleType(ImageView.ScaleType.FIT_XY);


    }

    /**
     * The method return the db object. it made static in order for other activites to use this method.
     * @return
     */
    public static AppDatabase getDB(){return db;}





////////////////////////////Asycn Tasks that are called in the SignIn Activity/////////////////////////////////////////

    /**
     * The async task is responsible to:
     * 1- get the username and password entered by the user.
     * 2- checks if the user name and password exists in the db.
     * 3- notify the user if the credentials dont exist by letting him sign up.
     * 4- if the credential are correct it let the user go to the main activity.
     *
     * The sign in activity is called when the user open the app and when the user clicks on
     * a notification to take his prescription.
     *
     * if the signin activity is called from the notification, before going to the main activity,
     * an intent is initilized carrying the user and the prescription the took. This is done in order
     * for the main activity process the prescription that the user took.
     *
     *
     */
    private  class signInAsyncTask extends AsyncTask<String[], Void, Boolean> {

        private User u;

        @Override
        /**
         * The method shows a waiting dialog to the user
         */
        protected void onPreExecute() {

            login.setEnabled(false);
            progressDialog = new ProgressDialog(SigninActivity.this,
                    R.style.Theme_AppCompat_DayNight_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();
        }
        @Override
        /**
         * Checks if the credential exists or not.
         */
        protected Boolean doInBackground(String[]... credentials) {

            String cre []= credentials[0];
            u= db.userDao().getSpecificUser(cre[0]);
            boolean flag=false;
            try {
                String decPass=AES.decrypt(u.getPassword());
                System.out.println("cree eq:"+decPass +"  "+cre[1]);
                if(decPass.equals(cre[1])){
                    flag= true;
                }
            }catch(Exception e){
             flag=false;
            }
            if(u==null)
                flag= false;

            return flag;
        }

        @Override
        /**
         * if the credentials are correct then go to the main activity.
         * if the signin activity is called from a notification then carry the user and the prescription
         * they took to the main activity.
         *
         */
        protected void onPostExecute(Boolean credentialResult) {

            progressDialog.dismiss();
            login.setEnabled(true);

            if(!credentialResult) {
                Snackbar.make(findViewById(android.R.id.content),
                        "No User Found/No such credentials, Please Sign Up!", Snackbar.LENGTH_LONG).show();
            }
            else{
                Intent intentFromNotification = getIntent();
                String precriptionName="";
                String prescriptionUser="";
                if(intentFromNotification!=null) {
                    precriptionName = intentFromNotification.getStringExtra("pname");
                    prescriptionUser = intentFromNotification.getStringExtra("puser");
                }



                Intent intentToMainActivity = new Intent( getApplicationContext(),MainActivity.class);
                intentToMainActivity.putExtra("user", u);
                intentToMainActivity.putExtra("prescriptionTaken", precriptionName);

                startActivity(intentToMainActivity);
            }





        }


    }


}
