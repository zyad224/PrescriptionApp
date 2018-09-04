package com.example.zeyad.prescriptionapp.Acitvities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.TimeZone;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.zeyad.prescriptionapp.Adapters.ViewPagerAdapter;
import com.example.zeyad.prescriptionapp.Database.AppDatabase;
import com.example.zeyad.prescriptionapp.Database.Doctor;
import com.example.zeyad.prescriptionapp.Database.DoseTime;
import com.example.zeyad.prescriptionapp.Database.Notification;
import com.example.zeyad.prescriptionapp.Database.Prescription;
import com.example.zeyad.prescriptionapp.Database.User;
import com.example.zeyad.prescriptionapp.R;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * This is the main activity of the the mobile app.
 * It initializes the interface of the app.
 * It sets the fragments of the app (home, history, new).
 * It sets the upcoming prescription list,
 * It sets the prescription takings chart.
 * It set the pop up menue for the user
 * It receives prescriptions taken by user from notifiation bar.
 * It re-calculate the total prescription doses left after the user took the prescription.
 * It fetches the signed in user.
 *
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
    public static List<PieEntry> chartEntries;



/////////////////////////Main Activity of the App starts here/////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivityGUI();
        userTakePrescription();
        userUpcomingPrescriptions();
        userPrescriptionPieChart();
        checkDoctorDetails();
        prescruptionRefillCheck();


    }



 ///////////////////////////////Main Activity methods/////////////////////////////////////////////////////////////////

    /**
     * The method is responsible to set the GUI of the main activity of the app.
     * It sets:
     * 1- the toolbar of the app.
     * 2- the pref which includes an integer(the latest notification id stored in the app).
     * 3- tablayout.
     * 4- the view pager for the fragments used in the app.
     * 5- the names of the different tabs.
     *
     */
    private void mainActivityGUI(){

        // set the app toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // set the app pref, tabs, view pager (for fragments), and pager adapter
        pref = getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        tabs=(TabLayout) findViewById(R.id.tabs);
        pager=(ViewPager) findViewById(R.id.viewPager);
        pagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());


        // synchronize the tabs with the view pager
        // load 2 pages hwne app starts ( setOffscreePageLimit) for performance
        pager.setAdapter(pagerAdapter);
        tabs.setupWithViewPager(pager);
        setTabLayoutIcons(tabs);
        pager.setOffscreenPageLimit(2);



        // set tab names when user clicks on a certain tab
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
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

    }

    /**
     * The method is responsible to set the chart of the prescriptions takings
     * and prepare the chart to be viewed in the Home Fragment.
     */
    private void userPrescriptionPieChart(){
        chartEntries=new ArrayList<>();
        new calculateUserPrescriptionTakingsPercentage().execute();

    }

    /**
     * The method is responsible to set the list of the upcoming prescriptions( prescriptions in 3 hours)
     * and prepare the upcoming list to be viewed in the Home Fragment.
     */
    private void userUpcomingPrescriptions(){


        upcomingPrescription=new ArrayList<String>();
        new fetchUserUpcomingPrescriptions().execute();

    }

    /**
     * The method is responsible to receive an intent from the sign in activity, get the signed in user,
     * and get the prescription name if the user clicked on the notification to take the prescription.
     *
     * If the user took the prescription (ie clicked on the notification to take it), then the async task
     * reducePrescriptionTakings is called to reduce the total amount of doses left for that prescription.
     *
     */
    private void userTakePrescription(){

        Intent intentFromSignInActivity = getIntent();
        signedInUser=(User)intentFromSignInActivity .getSerializableExtra("user");
        String prescriptionTakenFromNoti=intentFromSignInActivity .getStringExtra("prescriptionTaken");
        String prescriptionRefill=intentFromSignInActivity .getStringExtra("prescriptionRefill");


         System.out.println("see:"+prescriptionTakenFromNoti);
        System.out.println("see2:"+prescriptionRefill);


        if(prescriptionTakenFromNoti!=null &&prescriptionRefill==null) {
            new reducePrescriptionTakings().execute(prescriptionTakenFromNoti);
            prescriptionTakenFromNoti="";
            System.out.println("prescription taken main activity:" + prescriptionTakenFromNoti);


        }

        else if(prescriptionRefill!=null){
            System.out.println("refill needed ");
            prescriptionRefill="";

            refillPresciption(prescriptionTakenFromNoti);
        }



    }


    /**
     * The method is responsible to refill a specific prescription.
     *
     * It is executed when the user clicks on the notification and confirm that they refilled their app.
     * @param prescriptionToRefill
     */
    private void refillPresciption(String prescriptionToRefill){

        new refillPrescription().execute(prescriptionToRefill);


    }

    /**
     * The method  is responsible to run the DoctorActivity to the user to add thier doctors.
     *
     * The DoctorActivity is shown to the user when they run the app for the first time only.
     */
    private void checkDoctorDetails(){
        Boolean firstRun=pref.getBoolean("firstRun8",true);

        if(firstRun){
            startActivity(new Intent(MainActivity.this, DoctorActivity.class));

        }


    }

    /**
     * The method is responsible to run an async task to check if any prescription needs a refill or not
     *
     * If a prescription needs a refill (ie the doses are finished), a notification is generated instantly
     * to notify the user.
     *
     * In order for the user to refill a prescription, they must click on that notification and sign in to
     * confirm.
     */
    private void prescruptionRefillCheck(){

        new PrescriptionRefillNotification().execute();

    }

    /**
     * The method is responsible to set the icons of the tabs for different pages.
     * Pages:
     * 1- History.
     * 2- Home.
     * 3- New.
     * @param tabs
     */
    private void setTabLayoutIcons(TabLayout tabs){

        tabs.getTabAt(0).setIcon(tabIcons[0]);
        tabs.getTabAt(1).setIcon(tabIcons[1]);
        tabs.getTabAt(2).setIcon(tabIcons[2]);
    }




///////////////////////////////// The pop out menue methods///////////////////////////////////////////////////////////

    @Override
    /**
     * The method responsible to inflate the menue when user clicks on it.
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    /**
     * The method is responsible to fetch the id of the item chosen from the menue.
     * There are 3 choices:
     * 1- Nearby Hospitals.
     * 2- Nearby Doctors.
     * 3- Nearby Pharmacies.
     *
     * When any of the choices above clicked, a new map activity intent starts to show the nearby
     * { hostpitals, doctors, dentists, pharmacies}.
     *
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;

        switch (id){
            case (R.id.menue_hospitals):
                intent=new Intent(this, MapsActivity.class);
                intent.putExtra("searchKey","hospital");
                break;
            case (R.id.menue_doctor):
                intent=new Intent(this, MapsActivity.class);
                intent.putExtra("searchKey","doctor");
                break;

            case (R.id.menue_dentist):
                intent=new Intent(this, MapsActivity.class);
                intent.putExtra("searchKey","dentist");
                break;


            case (R.id.menue_pharmacy):
                intent=new Intent(this, MapsActivity.class);
                intent.putExtra("searchKey","pharmacy");
                break;


            case (R.id.menue_myDoctors):
                intent=new Intent(this, MyDoctors.class);
                break;
            default:
                return super.onOptionsItemSelected(item);


        }

        startActivity(intent);

        return true;
    }




//////////////////////////Asycn Tasks that are called when the app is started/////////////////////////////////////////

    /**
     * The async task is responsible to:
     * 1- Fetch all the prescription dose times of the signed in user.
     * 2- It iterates on all the dose times and creates a calender object for each dose time.
     * 3- pass the calender object to compareCurrentTimeWDoseTime to compare the dose time with the current time.
     * 4- If the difference between the current time and the dose time is 3 hours the add that prescription and dose time
     * to the upcoming prescription list.
     */
    private  class fetchUserUpcomingPrescriptions extends AsyncTask<Void, Void, Boolean> {

        List<DoseTime> prescriptionsDoseTimes=null;
        String [] doseTimings= new String [5];


        /**
         * The method compare between the current time and the dose time.
         * If the difference between them is 3 hours then add the prescription in the
         * upcoming prescription list.
         * @param doseTime
         * @return
         */
        private boolean compareCurrentTimeWDoseTime(Calendar doseTime){

            Calendar timeNow= Calendar.getInstance();
            long seconds=  (doseTime.getTimeInMillis()-timeNow.getTimeInMillis())/1000;
            int hours= (int) seconds/3600;

            int timeDiff=timeNow.compareTo(doseTime);

            System.out.println("timediff:"+ timeDiff);
            //time passed
            if(timeDiff>0){
                return false;
            }
            else{

                if(hours<=2&&hours>=0) {
                    return true;
                }
            }


            return false;
        }

        @Override
        /**
         * The method iterates on all the prescriptions dose times and pass them to the method
         * compareCurrentTimeWDoseTime to compare the dosetime with the current time.
         *
         * If the difference is 3 hours then add the prescription to the upcoming prescription list.
         */
        protected Boolean doInBackground(Void... voids) {


            AppDatabase db= SigninActivity.getDB();
            int maximiumDoses=5;
            prescriptionsDoseTimes=db.dosetimeDao().getAllDoseTimes(MainActivity.signedInUser.getUserName());
            upcomingPrescription.clear();
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

                           System.out.println(""+ t.getPrescription_name());

                           Prescription p=db.prescriptionDao().getSpecificPrescription
                                   (t.getPrescription_name(),MainActivity.signedInUser.getUserName());

                           upcomingPrescription.add(p.getPrescriptionName()+" - "+ p.getPrescriptionType()+ " - "+
                                   p.getPrescriptionDoese()+"Dose"+" - "+str);

                       }




                   }

               }


            }


            return true;
        }



    }


    /**
     *  The async taks is responsible to:
     *  1- Receive the user and name of the prescription taken by him when the user confirm he took the prescription.
     *  2- Fetch the prescription from database using the user id and the prescription name.
     *  3- Fetch the dose to be taken, the total amount of doses of that prescription, the number of dose already taken until now.
     *  4- It increments the number of doses already taken. for example:
     *
     *  if the dose to be taken is 2, the initial amount of doses is 30. if the user took the prescription,
     *  then the amount of dose already taken is: 0+2, 2+2, 4+2, 6+2, ..... until the inital amount of dose
     *  and dose already taken is 30 : 30>>> which means the prescription is finished and need refill.
     *
     *  5- It show the user the prescription he took and the amount of doses left for that prescription.
     *
     *  6- if the user is out of prescription, it notifies the user to refill their prescription.
     */
    private  class reducePrescriptionTakings extends AsyncTask<String, Void, Boolean> {

        Prescription prescriptionTakenByUser=null;
        int doseToBeTaken=0;
        int takingsBeforeDose=0;
        int takingsAfterDose=0;
        int initialPrescriptionTakings=0;


        @Override
        protected Boolean doInBackground(String... prescriptionDetails) {

            AppDatabase db= SigninActivity.getDB();
            prescriptionTakenByUser= db.prescriptionDao().
                    getSpecificPrescription(prescriptionDetails[0],signedInUser.getUserName());

            initialPrescriptionTakings=prescriptionTakenByUser.getTakings();
            doseToBeTaken= prescriptionTakenByUser.getPrescriptionDoese();
            takingsBeforeDose= prescriptionTakenByUser.getForgetTakings();
            takingsAfterDose= takingsBeforeDose+doseToBeTaken;

            System.out.println("in reduce:"+initialPrescriptionTakings+","+takingsAfterDose);

            if(takingsAfterDose<=initialPrescriptionTakings) {
               System.out.println("in reduce2:"+initialPrescriptionTakings+","+takingsAfterDose);
                db.prescriptionDao().updatePrescriptionTakings(takingsAfterDose,
                        prescriptionTakenByUser.getPrescriptionName(), signedInUser.getUserName());
            }
            else{
                return false;
            }





            return true;
        }


        @Override
        protected void onPostExecute(Boolean insertingResult) {

            if(insertingResult) {
                AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                adb.setTitle("Prescription Taken");
                adb.setMessage(" You have taken:" + doseToBeTaken+ " "+ "Doses from"+" "+prescriptionTakenByUser.getPrescriptionName()
                +"."+"\n"+ " You have:"+ Integer.toString(initialPrescriptionTakings-takingsAfterDose)+" "+"Doses left !!!");

                adb.setNegativeButton("Ok", null);
                adb.show();
            }
            else{
                AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                adb.setTitle("Prescription needs to be refilled");
                adb.setMessage(" You have to refill:"+prescriptionTakenByUser.getPrescriptionName()
                        +"\n"+"click on the notifiation to refill!!!");

                adb.setNegativeButton("Ok", null);
                adb.show();
            }


        }


    }

    /**
     *  The async task is responsible to:
     *  1- Fetch all the prescriptions of the user signed in to the app.
     *  2- Iterates on all the prescriptions and calculate the percentage of prescription taking for every prescription.
     *  3- It does so by calculating percentageOfTakings=((float)takingsUntilNow/(float)initialPrescriptionTakings)*100;
     *  4- Insert each prescription along with its percentage in the chart
     */
    private  class calculateUserPrescriptionTakingsPercentage extends AsyncTask<Void, Void, Boolean> {



        @Override
        protected Boolean doInBackground(Void... voids) {


            AppDatabase db= SigninActivity.getDB();
            List<Prescription> pres=db.prescriptionDao().getUserPrescription(MainActivity.signedInUser.getUserName());

            for(Prescription p:pres){
                int initialPrescriptionTakings=p.getTakings();
                int takingsUntilNow=p.getForgetTakings();
                float percentageOfTakings=((float)takingsUntilNow/(float)initialPrescriptionTakings)*100;
                chartEntries.add(new PieEntry(percentageOfTakings,p.getPrescriptionName()));
            }



            return true;
        }




    }


    /**
     * The async task is responsible to:
     * 1- Search all the user prescriptions.
     * 2- if any prescription needs to refill. it generate an instant notification to notify the user.
     *
     * Note: the notification will not stop to pop to the user until the user clicks on it and refill
     * their prescriptions.
     */
    private  class PrescriptionRefillNotification extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected Boolean doInBackground(Void... voids) {


            AppDatabase db = SigninActivity.getDB();
            List<Prescription> pres = db.prescriptionDao().getUserPrescription(MainActivity.signedInUser.getUserName());

            for (Prescription p : pres) {

                int initialPrescriptionTakings = p.getTakings();
                int takingsUntilNow = p.getForgetTakings();
                if ((initialPrescriptionTakings - takingsUntilNow) == 0) {
                    System.out.println("in noti 2");
                    Notification n = new Notification(p, getApplicationContext());

                }

            }

            return true;

        }
    }


    /**
     * The async task is responsible to:
     * 1- fetch a specific prescription from the db.
     * 2- refill the prescription by setting the user takingsUntillnow back to zero(forgetTakings in the db scheme).
     * 3- notify the user that they refilled their prescriptions.
     */
    private  class refillPrescription extends AsyncTask<String, Void, Boolean> {


        Prescription p;
        @Override
        protected Boolean doInBackground(String... pres) {


            AppDatabase db = SigninActivity.getDB();
            String prescriptionName=pres[0];
            p= db.prescriptionDao().getSpecificPrescription(prescriptionName,MainActivity.signedInUser.getUserName());
            db.prescriptionDao().updatePrescriptionTakings(0,prescriptionName,MainActivity.signedInUser.getUserName());

            return true;

        }

        @Override
        protected void onPostExecute(Boolean insertingResult) {

            if(insertingResult) {
                AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                adb.setTitle("Prescription Refilled");
                adb.setMessage(" You have refilled:" + p.getPrescriptionName()+" , "+ p.getPrescriptionType()+"."+"\n"+
                        " You have:"+ p.getTakings()+" "+"Doses left !!!");

                adb.setNegativeButton("Ok", null);
                adb.show();
            }


        }
    }

    @Override
    /**
     * The method minimize the app to the user when they try to go back to the sign in activity.
     */
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}

