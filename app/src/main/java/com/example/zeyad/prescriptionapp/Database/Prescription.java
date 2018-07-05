package com.example.zeyad.prescriptionapp.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Zeyad on 7/5/2018.
 */

@Entity(foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "id",
        childColumns = "location_id",
        onDelete=CASCADE))
public class Prescription {
}
