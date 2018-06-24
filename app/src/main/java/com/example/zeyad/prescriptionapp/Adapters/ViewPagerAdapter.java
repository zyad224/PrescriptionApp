package com.example.zeyad.prescriptionapp.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.zeyad.prescriptionapp.Fragments.AddPrescription;
import com.example.zeyad.prescriptionapp.Fragments.Home;
import com.example.zeyad.prescriptionapp.Fragments.PrescriptionLog;

/**
 * Created by Zeyad on 6/23/2018.
 *
 * This class is used to generates different fragments depending on
 * which page the user swipes to. {Prescription Log, Home, Add a Prescription}.
 *
 * It consists of 3 methods:
 *
 * getItem(int position): return the fragment depending on which tab the user selects.
 * getCount(): return the number of fragments.
 * getPageTitile(int position): write the names of the tabs in the tabLayout.
 *
 *
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    /**
     * This method return the fragment depending on which tab the user selects.
     */
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
    /**
     * This method return the number of fragments.
     */
    public int getCount() {
        return 3;
    }

    @Override
    /**
     * This method write the names of tabs in the tablayout
     */
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "History";
        }
        else if (position == 1)
        {
            title = "Home";
        }
        else if (position == 2)
        {
            title = "New";
        }
        return title;
    }
}
