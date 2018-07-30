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


/**
 * This is the sign up activity of the the mobile app.
 * It initializes the interface of the sign up activity .
 * It checks user input username, password, etc.
 * It notifies the user if the a new account is create to them or not (already exists)
 * It directs the user either to the sign in activity to sign in.
 *
 *
 */
public class SignupActivity extends AppCompatActivity {

    private EditText name;
    private EditText userName;
    private EditText password;
    private Button signup;
    private TextView tv;
    private ImageView img;
    private String Name,UserName,Password;
    private ProgressDialog progressDialog;


///////////////////////////////SignUp Activity starts here/////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupGUI();



    }



/////////////////////////////////SignUp Activity methods/////////////////////////////////////////////////////////////////

    /**
     *
     * The method is responsible to set the GUI of the sign up activity of the app.
     * It sets:
     * 1- the username edit text.
     * 2- the password edit text.
     * 3- the name of the user edit text.
     * 4- the signup button.
     * 5- the icon of the app.
     * 6- set click listeners on the create account button and the already a memebertext view.
     * 7- checks if the new credentials of the user doesnt exist already in the db or not.
     *
     */
    public void signupGUI(){

        // set different attributes
        name=(EditText) findViewById(R.id.input_name);
        userName=(EditText) findViewById(R.id.input_username);
        password=(EditText) findViewById(R.id.input_password);
        signup=(Button) findViewById(R.id.signup);
        tv=(TextView) findViewById(R.id.alreadyMember);
        img=(ImageView) findViewById(R.id.appLogo);

        //set the color of the create account button and size of app icon

        int red=Color.rgb(196,3,3);
        signup.setBackgroundColor(red);
        signup.setTextColor(Color.WHITE);
        resizeAppIcon(img);


        // set the click listener on the create account button
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //checks username and pass already exists
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
                // go to the sign in activity
                finish();
            }
        });

    }

    /**
      The method is responsible to resize the app icon 400x400 pixels
     * @param img
     */
    public void resizeAppIcon(ImageView img){
        int newHeight = 400;
        int newWidth = 400;
        img.requestLayout();
        img.getLayoutParams().height = newHeight;

        // Apply the new width for ImageView programmatically
        img.getLayoutParams().width = newWidth;

        // Set the scale type for ImageView image scaling
        img.setScaleType(ImageView.ScaleType.FIT_XY);


    }

    /**
     * The method checks if the user has input their name, username, and password or not
     * @return
     */
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


////////////////////////////Asycn Tasks that are called in the Signup Activity/////////////////////////////////////////


    /**
     * * The async task is responsible to:
     * 1- get the username ,password, and name of the new user.
     * 2- checks if the user already exists or not in the db.
     * 3- if the user doesnt exits then create a new account for them.
     * 4- if the exists then notify the user to login.
     *
     *
     */
    private  class signUpAsyncTask extends AsyncTask<String[], Void, Boolean> {

        private User u;

        @Override
        /*
          The method shows a waiting dialog to the user
         */
        protected void onPreExecute() {

            signup.setEnabled(false);
            progressDialog = new ProgressDialog(SignupActivity.this,
                    R.style.Theme_AppCompat_DayNight_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating an Account...");
            progressDialog.show();
        }
        @Override
        /*
          The method checks if the user alredy exists or not.
          If not then create a new account.
          If user already exists notify the user
         */
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
        /**
         * Notify the user if the account is created or that it already exists.
         */
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
