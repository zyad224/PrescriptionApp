package com.example.zeyad.prescriptionapp;

import org.junit.Test;

import static org.junit.Assert.*;
/**
 * Created by Zeyad on 8/11/2018.
 */

public class SignUpTesting {

    private String validateUserinput(String name, String username, String password){


        if(!name.isEmpty()&&!username.isEmpty()&&!password.isEmpty()) {
            return "1";
        }
        else {
            if (name.isEmpty())
                return "Please Add your name";


            else if (username.isEmpty())
                return "Please Add your username";

            else if (password.isEmpty())
                return "Please Add your Password";
        }
        return "0";
    }

    @Test
    public void validateInputNotEmpty() throws Exception {

        String resultofValidation=validateUserinput("zyad","zyad224","123");
        assertEquals("1", resultofValidation);
    }
    @Test
    public void validateInputNameEmpty() throws Exception {

        String resultofValidation=validateUserinput("","zyad224","123");
        assertEquals("Please Add your name", resultofValidation);
    }
    @Test
    public void validateInputUserNameEmpty() throws Exception {

        String resultofValidation=validateUserinput("zyad","","123");
        assertEquals("Please Add your username", resultofValidation);
    }
    @Test
    public void validateInputPassEmpty() throws Exception {

        String resultofValidation=validateUserinput("zyad","zyad224","");
        assertEquals("Please Add your Password", resultofValidation);
    }
}
