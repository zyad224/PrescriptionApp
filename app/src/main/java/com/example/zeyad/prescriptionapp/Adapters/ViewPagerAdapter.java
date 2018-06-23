package com.example.zeyad.prescriptionapp.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.zeyad.prescriptionapp.Fragments.AddPrescription;
import com.example.zeyad.prescriptionapp.Fragments.Home;
import com.example.zeyad.prescriptionapp.Fragments.PrescriptionLog;

/**
 * Created by Zeyad on 6/23/2018.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment newFragment=null;

        if(position==0)
            newFragment= new PrescriptionLog();
        else if(position==1)
            newFragment=new Home();
        else if(position==2)
            newFragment= new AddPrescription();

        return newFragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "Prescription Log";
        }
        else if (position == 1)
        {
            title = "Home";
        }
        else if (position == 2)
        {
            title = "Add a Prescription";
        }
        return title;
    }
}
