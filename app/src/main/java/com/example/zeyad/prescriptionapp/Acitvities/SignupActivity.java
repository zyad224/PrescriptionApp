package com.example.zeyad.prescriptionapp.Acitvities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zeyad.prescriptionapp.Database.AppDatabase;
import com.example.zeyad.prescriptionapp.Database.User;
import com.example.zeyad.prescriptionapp.R;


import static android.app.PendingIntent.getActivity;

public class SignupActivity extends AppCompatActivity {

    private EditText name;
    private EditText userName;
    private EditText password;
    private Button signup;
    private TextView tv;
    private ImageView img;
    private String Name,UserName,Password;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name=(EditText) findViewById(R.id.input_name);
        userName=(EditText) findViewById(R.id.input_username);
        password=(EditText) findViewById(R.id.input_password);
        signup=(Button) findViewById(R.id.signup);
        tv=(TextView) findViewById(R.id.alreadyMember);
        img=(ImageView) findViewById(R.id.appLogo);

        int red=Color.rgb(196,3,3);
        signup.setBackgroundColor(red);
        signup.setTextColor(Color.WHITE);

        resizeAppIcon(img);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validateUserinput()){
                    //async task
                    String[] credentials =new String [3] ;
                    credentials[0]=Name;
                    credentials[1]=UserName;
                    credentials[2]=Password;
                    new signUpAsyncTask().execute(credentials);

                }

            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });



    }

    public static void resizeAppIcon(ImageView img){
        int newHeight = 400;
        int newWidth = 400;
        img.requestLayout();
        img.getLayoutParams().height = newHeight;

        // Apply the new width for ImageView programmatically
        img.getLayoutParams().width = newWidth;

        // Set the scale type for ImageView image scaling
        img.setScaleType(ImageView.ScaleType.FIT_XY);


    }
    private boolean validateUserinput(){
        Name= name.getText().toString();
        UserName=userName.getText().toString();
        Password=password.getText().toString();

        if(!Name.isEmpty()&&!UserName.isEmpty()&&!Password.isEmpty()) {
            return true;
        }
        else {
            if (Name.isEmpty())
                Snackbar.make(findViewById(android.R.id.content),
                        "Please Add your name", Snackbar.LENGTH_LONG).show();

            else if (UserName.isEmpty())
                Snackbar.make(findViewById(android.R.id.content),
                        "Please Add your username", Snackbar.LENGTH_LONG).show();

            else if (Password.isEmpty())
                Snackbar.make(findViewById(android.R.id.content),
                        "Please Add your Password", Snackbar.LENGTH_LONG).show();
        }
        return false;
    }

    private  class signUpAsyncTask extends AsyncTask<String[], Void, Boolean> {

        private User u;

        @Override
        protected void onPreExecute() {

            signup.setEnabled(false);
            progressDialog = new ProgressDialog(SignupActivity.this,
                    R.style.Theme_AppCompat_DayNight_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating an Account...");
            progressDialog.show();
        }
        @Override
        protected Boolean doInBackground(String[]... credentials) {

            String cre []= credentials[0];
            AppDatabase db= SigninActivity.getDB();
            u= db.userDao().getSpecificUser(cre[1]);
            if(u==null) {
                User newUser = new User(cre[1], cre[2], cre[0]);
                db.userDao().insertUser(newUser);
                return true;
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean signUpResult) {

            progressDialog.dismiss();
            signup.setEnabled(true);

            if(signUpResult) {
                Snackbar.make(findViewById(android.R.id.content),
                        "your account has been created", Snackbar.LENGTH_LONG).show();
            }
            else{
                Snackbar.make(findViewById(android.R.id.content),
                        "This account already exists. Please sign in", Snackbar.LENGTH_LONG).show();
            }




        }


    }
}
