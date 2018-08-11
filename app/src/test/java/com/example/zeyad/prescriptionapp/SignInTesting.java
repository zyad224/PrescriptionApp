package com.example.zeyad.prescriptionapp;

import com.example.zeyad.prescriptionapp.Acitvities.SigninActivity;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Zeyad on 8/11/2018.
 */

public class SignInTesting {

    private boolean validateUserinput(String Username,String Password){


        if(!Username.isEmpty()&&!Password.isEmpty()) {
            return true;
        }

        return false;
    }

    @Test
    public void validateUsernamePassEmpty() throws Exception {

        boolean resultofValidation=validateUserinput(""," ");
        assertEquals(false, resultofValidation);
    }

    @Test
    public void validateUsernamePassNotEmpty() throws Exception {

        boolean resultofValidation=validateUserinput("zyad","123");
        assertEquals(true, resultofValidation);
    }

    @Test
    public void validateUsernamePassUsernameEmpty() throws Exception {

        boolean resultofValidation=validateUserinput("","123");
        assertEquals(false, resultofValidation);
    }

    @Test
    public void validateUsernamePassPasswordEmpty() throws Exception {

        boolean resultofValidation=validateUserinput("zyad","");
        assertEquals(false, resultofValidation);
    }
}
