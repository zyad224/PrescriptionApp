package com.example.zeyad.prescriptionapp;

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

import static com.example.zeyad.prescriptionapp.SignupActivity.resizeAppIcon;

public class SigninActivity extends AppCompatActivity {

    private EditText userName;
    private EditText password;
    private Button login;
    private TextView tv;
    private ImageView img;
    private String UserName,Password;
    private ProgressDialog progressDialog;

    public static AppDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        userName=(EditText) findViewById(R.id.input_username_signin);
        password=(EditText) findViewById(R.id.input_password_signin);
        login=(Button) findViewById(R.id.login);
        tv=(TextView) findViewById(R.id.signup_link);
        img=(ImageView) findViewById(R.id.appLogo);

        int red= Color.rgb(196,3,3);
        login.setBackgroundColor(red);
        login.setTextColor(Color.WHITE);
        resizeAppIcon(img);

        // control of database
        if (db==null) {
            db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "app_db2")
                    .fallbackToDestructiveMigration()
                    .build();
            Log.d("db", "onCreate: db");
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validateUserinput()){
                    //async task
                    String[] credentials =new String [2] ;
                    credentials[0]=UserName;
                    credentials[1]=Password;
                    new signInAsyncTask().execute(credentials);
                }

            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent( getApplicationContext(),SignupActivity.class);
                startActivity(intent);
            }
        });


    }



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

    public static AppDatabase getDB(){return db;}


    private  class signInAsyncTask extends AsyncTask<String[], Void, Boolean> {

        private User u;

        @Override
        protected void onPreExecute() {

            login.setEnabled(false);
            progressDialog = new ProgressDialog(SigninActivity.this,
                    R.style.Theme_AppCompat_DayNight_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();
        }
        @Override
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
        protected void onPostExecute(Boolean credentialResult) {

            progressDialog.dismiss();
            login.setEnabled(true);

            if(!credentialResult) {
                Snackbar.make(findViewById(android.R.id.content),
                        "No User Found/No such credentials, Please Sign Up!", Snackbar.LENGTH_LONG).show();
            }
            else{
                Intent intent = new Intent( getApplicationContext(),MainActivity.class);
                intent.putExtra("user", u);
                startActivity(intent);
            }





        }


    }
}
