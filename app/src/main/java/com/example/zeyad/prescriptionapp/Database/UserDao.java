package com.example.zeyad.prescriptionapp.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Zeyad on 7/8/2018.
 *This class provides the different sql queries to the entity User.
 */

@Dao
public interface UserDao {

    @Query("SELECT * FROM User")
    public List<User> getAllUsers ();

    @Query("SELECT * FROM User WHERE userName LIKE :username")
    public User getSpecificUser (String username);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);
}
