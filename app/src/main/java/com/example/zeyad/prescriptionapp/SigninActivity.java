package com.example.zeyad.prescriptionapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.zeyad.prescriptionapp.SignupActivity.resizeAppIcon;

public class SigninActivity extends AppCompatActivity {

    private EditText userName;
    private EditText password;
    private Button login;
    private TextView tv;
    private ImageView img;
    private String UserName,Password;
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

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateUserinput();
                //send to db

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
}
