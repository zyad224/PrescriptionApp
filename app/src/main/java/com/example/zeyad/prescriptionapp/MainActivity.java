package com.example.zeyad.prescriptionapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.example.zeyad.prescriptionapp.Adapters.ViewPagerAdapter;
import com.example.zeyad.prescriptionapp.Database.AES;
import com.example.zeyad.prescriptionapp.Database.AppDatabase;
import com.example.zeyad.prescriptionapp.Database.DoseTime;
import com.example.zeyad.prescriptionapp.Database.Prescription;
import com.example.zeyad.prescriptionapp.Database.User;
import com.example.zeyad.prescriptionapp.Fragments.AddPrescription;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    public static final String prefs_name="MyPrefs";
    public static SharedPreferences pref;
    public static ArrayList<String> upcomingPrescription;
    private SwipeRefreshLayout SwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        pref = getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        tabs=(TabLayout) findViewById(R.id.tabs);
        pager=(ViewPager) findViewById(R.id.viewPager);
        pagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        //SwipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.swiperefresh);



        pager.setAdapter(pagerAdapter);
        tabs.setupWithViewPager(pager);
        setTabLayoutIcons(tabs);
        pager.setOffscreenPageLimit(2);



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


        userTakePrescription();
        userUpcomingPrescriptions();

    }



    private void userUpcomingPrescriptions(){


        upcomingPrescription=new ArrayList<String>();
        new fetchUserUpcomingPrescriptions().execute();

    }
    private void userTakePrescription(){

        Intent intentFromSignInActivity = getIntent();
        signedInUser=(User)intentFromSignInActivity .getSerializableExtra("user");
        String prescriptionTakenFromNoti=intentFromSignInActivity .getStringExtra("prescriptionTaken");



        if(prescriptionTakenFromNoti!=null) {
            new reducePrescriptionTakings().execute(prescriptionTakenFromNoti);
            System.out.println("prescription taken main activity:" + prescriptionTakenFromNoti);


        }

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
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    private void setTabLayoutIcons(TabLayout tabs){

        tabs.getTabAt(0).setIcon(tabIcons[0]);
        tabs.getTabAt(1).setIcon(tabIcons[1]);
        tabs.getTabAt(2).setIcon(tabIcons[2]);
    }



    public  SwipeRefreshLayout getSwipeRefreshLayout(){
        return this.SwipeRefreshLayout;
    }



    private  class fetchUserUpcomingPrescriptions extends AsyncTask<Void, Void, Boolean> {

        List<DoseTime> prescriptionsDoseTimes=null;
        String [] doseTimings= new String [5];
        ArrayList<String> upcomingPres=new ArrayList<String>();
        ArrayList<String> upcomingPresDose=new ArrayList<String>();



        private boolean compareCurrentTimeWDoseTime(Calendar doseTime){

            Calendar timeNow= Calendar.getInstance();
            long seconds=  (doseTime.getTimeInMillis()-timeNow.getTimeInMillis())/1000;
            int hours= (int) seconds/3600;

            if(hours<=2) {
                System.out.println("hours in="+ hours);

                return true;
            }

            return false;
        }
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected Boolean doInBackground(Void... voids) {


            AppDatabase db= SigninActivity.getDB();
            int maximiumDoses=5;
            prescriptionsDoseTimes=db.dosetimeDao().getAllDoseTimes(MainActivity.signedInUser.getUserName());

            for(DoseTime t: prescriptionsDoseTimes){
                doseTimings[maximiumDoses-5]=t.getDoseTime1();
                doseTimings[maximiumDoses-4]=t.getDoseTime2();
                doseTimings[maximiumDoses-3]=t.getDoseTime3();
                doseTimings[maximiumDoses-2]=t.getDoseTime4();
                doseTimings[maximiumDoses-1]= t.getDoseTime5();



               for(String str: doseTimings){
                   if(!str.isEmpty()){
                       String Timings[]=str.split(":");

                       Calendar timeOfDose=Calendar.getInstance();
                       timeOfDose.set(Calendar.HOUR,Integer.parseInt(Timings[0]));
                       timeOfDose.set(Calendar.MINUTE,Integer.parseInt(Timings[1]));

                       if(Timings[2].equalsIgnoreCase("AM"))
                           timeOfDose.set(Calendar.AM_PM,Calendar.AM);
                       else
                           timeOfDose.set(Calendar.AM_PM,Calendar.PM);

                       if(compareCurrentTimeWDoseTime(timeOfDose)){
                           System.out.println(t.getPrescription_name());
                           System.out.println(str);
//                           upcomingPres.add(t.getPrescription_name());
//                           upcomingPresDose.add(str);
                           upcomingPrescription.add(t.getPrescription_name()+" - "+ str);

                       }



                   }

               }


            }


            return true;
        }


        @Override
        protected void onPostExecute(Boolean insertingResult) {




        }


    }

    private  class reducePrescriptionTakings extends AsyncTask<String, Void, Boolean> {

        Prescription prescriptionTakenByUser=null;
        int doseToBeReduced=0;
        int amountOfTakingsLeft;
        int totalPrescriptionTakings=0;

        @Override
        protected void onPreExecute() {

        }
        @Override
        protected Boolean doInBackground(String... prescriptionDetails) {

            AppDatabase db= SigninActivity.getDB();
            prescriptionTakenByUser= db.prescriptionDao().
                    getSpecificPrescription(prescriptionDetails[0],signedInUser.getUserName());

            //System.out.println("before updating:"+ prescriptionTakenByUser.getTakings()+" "+prescriptionTakenByUser.getForgetTakings());
            doseToBeReduced= prescriptionTakenByUser.getPrescriptionDoese();
            amountOfTakingsLeft= prescriptionTakenByUser.getForgetTakings();
            totalPrescriptionTakings= amountOfTakingsLeft-doseToBeReduced;
            db.prescriptionDao().updatePrescriptionTakings(totalPrescriptionTakings,
                    prescriptionTakenByUser.getPrescriptionName(),signedInUser.getUserName());



            return true;
        }


        @Override
        protected void onPostExecute(Boolean insertingResult) {

            if(insertingResult) {
                AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                adb.setTitle("Prescription Taken");
                adb.setMessage(" You have taken:" + doseToBeReduced+ " "+ "Doses from"+" "+prescriptionTakenByUser.getPrescriptionName()
                +"."+ " You have:"+ totalPrescriptionTakings+" "+"Doses left !!!");

                adb.setNegativeButton("Ok", null);
                adb.show();
            }


        }


    }

}

