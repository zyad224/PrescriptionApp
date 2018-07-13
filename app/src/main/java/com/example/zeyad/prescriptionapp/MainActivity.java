package com.example.zeyad.prescriptionapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.zeyad.prescriptionapp.Adapters.ViewPagerAdapter;
import com.example.zeyad.prescriptionapp.Database.AES;
import com.example.zeyad.prescriptionapp.Database.User;
import com.example.zeyad.prescriptionapp.Fragments.AddPrescription;

/**
 * This is the main activity of the the mobile app.
 * It initializes the interface of the app.
 *
 * It consists of:
 * Toolbar.
 * TabLayout.
 * ViewPager.
 * ViewPagerAdapter.
 */
public class MainActivity extends AppCompatActivity {

    private  Toolbar toolbar;
    private TabLayout tabs;
    private ViewPager pager;
    private ViewPagerAdapter pagerAdapter;
    private int[] tabIcons={
            R.drawable.ic_prescription_history,
            R.drawable.ic_home_icon,
            R.drawable.ic_add_prescription
    };

    public static User signedInUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabs=(TabLayout) findViewById(R.id.tabs);
        pager=(ViewPager) findViewById(R.id.viewPager);
        pagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());


        pager.setAdapter(pagerAdapter);
        tabs.setupWithViewPager(pager);
        setTabLayoutIcons(tabs);
      // pager.setOffscreenPageLimit(2);



        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("zy:","pos:"+tab.getPosition());
                pager.setCurrentItem(tab.getPosition());


                if(tab.getPosition()==0)
                    toolbar.setTitle("My Prescriptions");
                else if(tab.getPosition()==1)
                    toolbar.setTitle("Home");

                else if(tab.getPosition()==2)
                    toolbar.setTitle("New Prescription");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                if(tab.getPosition()==0)
                    toolbar.setTitle("My Prescriptions");
                else if(tab.getPosition()==1)
                    toolbar.setTitle("Home");
                else if(tab.getPosition()==2)
                    toolbar.setTitle("New Prescription");
            }


        });

        Intent intent = getIntent();
         signedInUser=(User)intent.getSerializableExtra("user");
//        try{
//            Log.d("u passwrod main", "onCreate: "+ AES.decrypt(signedInUser.getPassword())+" -"+signedInUser.getUserName());
//
//
//        }catch(Exception e){
//
//        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setTabLayoutIcons(TabLayout tabs){

        tabs.getTabAt(0).setIcon(tabIcons[0]);
        tabs.getTabAt(1).setIcon(tabIcons[1]);
        tabs.getTabAt(2).setIcon(tabIcons[2]);
    }
}
