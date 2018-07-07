package com.example.zeyad.prescriptionapp.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by Zeyad on 7/5/2018.
 */

@Entity
public class User {



    @PrimaryKey
    @NonNull public String userName;

    @ColumnInfo(name="password")
    public String password;

    @ColumnInfo(name="name")
    public String name;

    @Ignore
    public String encryptedPassword;

    @Ignore
    public String decryptedPassword;

    public User(String userName,String password, String name){

        try {
            encryptedPassword=AES.encrypt(password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.userName=userName;
        this.password=encryptedPassword;
        this.name=name;
    }

    public String getUserName(){
        return this.userName;
    }

    public String getPassword(){
        try {
            decryptedPassword = AES.decrypt(this.password);
        }catch (Exception e){
            e.printStackTrace();
        }
        return decryptedPassword;
    }

    public String getName(){
        return this.name;
    }

}
