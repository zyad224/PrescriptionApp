package com.example.zeyad.prescriptionapp.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.zeyad.prescriptionapp.Adapters.ListAdapterAddPres;
import com.example.zeyad.prescriptionapp.Database.AppDatabase;
import com.example.zeyad.prescriptionapp.Database.DoseTime;
import com.example.zeyad.prescriptionapp.Database.Notification;
import com.example.zeyad.prescriptionapp.Database.Prescription;
import com.example.zeyad.prescriptionapp.Database.User;
import com.example.zeyad.prescriptionapp.Acitvities.MainActivity;
import com.example.zeyad.prescriptionapp.R;
import com.example.zeyad.prescriptionapp.Acitvities.SigninActivity;

import java.util.ArrayList;


/**
 * This is the add prescription fragment.
 * It initializes the interface of the add prescription fragment .
 * It provides a form to add a prescription details.
 * User can add doses times to the prescription being added.
 * After adding the prescription, a notification is generated for each dose time of that prescription.
 *
 * The interface is user-friendly and can be extended easily.
 * .
 *
 */
public class AddPrescription extends Fragment  {

    private Spinner DoseSpinner,PrescripitonTypeSpinner;
    private TimePicker tp;
    private FloatingActionButton TimeDosefab,Resetfab,insertPresInDB;
    private ArrayAdapter presTypeAdapter;
    private ArrayAdapter presDoseAdapter;
    private ListAdapterAddPres ListTimeDoseAdapter;
    private ListView TimeDoselist;
    private ArrayList<Prescription> arrayList;
    private String[] DoseAmountArray={"Dose","1","2","3","4"};
    private String[] PrescriptionTypeArray={"Type","Pills","Syrup","Eyedrops","Injection"};
    private String hours,minutes,am_pm;
    private String PrescriptionName, NumberOFTakings,DoctorName,DoctorNumber;
    private String PrescriptionType, PrescriptionDose;
    private EditText presName,takings,docName,docNumber;
    static int maximumSizeOfTimeDoseList=5;
    private ProgressDialog progressDialog;


    private View view;

    public AddPrescription() {
        // Required empty public constructor

    }


/////////////////////////////AddPrescription Fragment starts here/////////////////////////////////////////////////////////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_prescription, container, false);
        addPrescriptionGUI(view);
        return view;
    }


//////////////////////////////////AddPrescription Fragment methods//////////////////////////////////////////////////////////

    /**
     * The method is responsible to set the interface of AddPres Fragment:
     * 1- it initialize the edit text fields in the fragment
     * 2- it initialize 2 spinners {timedose and prescription type}
     * 3- it initialize the the time picker to choose time from.
     * @param view
     */
    private void addPrescriptionGUI(View view){

        initializeEditText(view);
        initializePrescriptionTypeSpinner(view);
        initializeTimePicker(view);
        initializeDoseAmountSpinner(view);
        initializeTimeDoseList(view);
        fabListeners();


    }

    /**
     * The method initialize the edit text fields in the fragment
     * @param view
     */
    private void initializeEditText(View view){
        presName=(EditText)view.findViewById(R.id.PrescriptionName);
        docName=(EditText)view.findViewById(R.id.DoctorName);
        docNumber=(EditText)view.findViewById(R.id.DoctorNumber);
        takings=(EditText)view.findViewById(R.id.Takings);
    }

    /**
     * The method initialize the prescription type spinner and sets its adapter.
     *
     * @param view
     */
    private void  initializePrescriptionTypeSpinner(View view){
        PrescripitonTypeSpinner = (Spinner) view.findViewById(R.id.PrescriptionType);
        presTypeAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,PrescriptionTypeArray);
        presTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PrescripitonTypeSpinner.setAdapter(presTypeAdapter);

        PrescripitonTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Log.d("type", "onItemSelected: "+PrescriptionTypeArray[position] );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    /**
     * The method initialize the time picker for choosing time
     * @param view
     */
    private void initializeTimePicker(View view){

        tp = (TimePicker)view.findViewById(R.id.Time);
        tp.setIs24HourView(true);

        tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                //Display the new time to app interface
                String AMPM = "AM";
                if(hourOfDay>11)
                {
                    hourOfDay = hourOfDay-12;
                    AMPM = "PM";
                }
                hours=String.valueOf(hourOfDay);
                minutes=String.valueOf(minute);
                am_pm=String.valueOf(AMPM);
            }
        });
    }

    /**
     * The method initialize the spinner for choosing the amount of dose and its adapter.
     * @param view
     */
    private void initializeDoseAmountSpinner(View view){
        DoseSpinner = (Spinner) view.findViewById(R.id.Dose);
        presDoseAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,DoseAmountArray);
        presDoseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DoseSpinner.setAdapter(presDoseAdapter);

        DoseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Log.d("type", "onItemSelected: "+DoseAmountArray[position] );

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    /**
     * The method initialize the time dose list of the prescription added.
     * It set the adapter of the list, the timedose button, and the reset button.
     *
     * It also enables deleting rows from the list by clicking ont the item to delete.
     *
     * @param view
     */
    private void initializeTimeDoseList(View view){



        TimeDoselist = (ListView) view.findViewById(R.id.TimeDoseList);
        TimeDosefab= (FloatingActionButton) view.findViewById(R.id.AddTimeDoseFab);
        Resetfab=(FloatingActionButton)view.findViewById(R.id.ResetFab);
        insertPresInDB=(FloatingActionButton)view.findViewById(R.id.insertPresInDB);


        arrayList = new ArrayList<Prescription>();
        ListTimeDoseAdapter=new ListAdapterAddPres(this.getContext(), android.R.layout.simple_list_item_1, arrayList);
        TimeDoselist.setAdapter(ListTimeDoseAdapter);

        TimeDoselist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                AlertDialog.Builder adb=new AlertDialog.Builder(getContext());
                adb.setTitle("Delete");
                adb.setMessage("Are you sure you want to delete this Time and Dose ? ");
                final int positionToRemove = position;
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        arrayList.remove(positionToRemove);
                        ListTimeDoseAdapter.updateList();
                        maximumSizeOfTimeDoseList++;
                        Log.d("maxR", "list max: "+maximumSizeOfTimeDoseList);

                    }});
                adb.show();
            }
        });



    }

    /**
     * The method adds clickListeners to the buttons on the fragment.
     * 3 buttons available:
     * 1- TimeDosefab: add time of doses to the list.
     * 2- Resetfab: clear the form to enter a new prescription.
     * 3- insertPres: add a new prescription to the db.
     */
    private void fabListeners(){

        TimeDosefab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation anim = android.view.animation.AnimationUtils.loadAnimation(TimeDosefab.getContext(),R.anim.shake);
                anim.setDuration(200L);
                TimeDosefab.startAnimation(anim);

                PrescriptionName=presName.getText().toString();
                DoctorName=docName.getText().toString();
                DoctorNumber=docNumber.getText().toString();
                NumberOFTakings=takings.getText().toString();
                PrescriptionType= PrescripitonTypeSpinner.getSelectedItem().toString();
                PrescriptionDose = DoseSpinner.getSelectedItem().toString();

                // check if the user filled all the fields in the form
                // if yes disable form and add doses to the list
                if((!PrescriptionDose.equals("Dose"))&& (hours!=null&&minutes!=null&&am_pm!=null)
                        && (!PrescriptionType.equals("Type"))&&(!PrescriptionName.isEmpty()
                         && !NumberOFTakings.isEmpty()))
                {
                    enableForm(false);
                    addPrescriptionListView();
                }
                // notify user to fill missing field
                else
                {
                    fillMissingFields();

                }

            }
        });

        Resetfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation anim = android.view.animation.AnimationUtils.loadAnimation(TimeDosefab.getContext(),R.anim.shake2);
                anim.setDuration(200L);
                Resetfab.startAnimation(anim);
                clearGUIElements();

            }
        });

        insertPresInDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!arrayList.isEmpty()) {
                    Animation anim = android.view.animation.AnimationUtils.loadAnimation(TimeDosefab.getContext(), R.anim.shake);
                    anim.setDuration(200L);
                    insertPresInDB.startAnimation(anim);
                   new addPrescriptionAsyncTask().execute(arrayList);


                }
                else{

                    Snackbar.make(insertPresInDB,
                            "Please fill the form and add your Time/Doses", Snackbar.LENGTH_LONG).show();
                }
               // clearGUIElements();

            }
        });

    }


    /**
     * The method enable/disable the add prescription form.
     * @param decision
     */
    private void enableForm(boolean decision){

        presName.setEnabled(decision);
        docName.setEnabled(decision);
        docNumber.setEnabled(decision);
        takings.setEnabled(decision);
        PrescripitonTypeSpinner.setEnabled(decision);
        DoseSpinner.setEnabled(decision);

    }

    /**
     * The method notify user to fill missing fields in the add prescription form.
     */
    private void fillMissingFields(){
        if(PrescriptionName.isEmpty()){
            Snackbar.make(insertPresInDB,
                    "Please Add a Prescription Name", Snackbar.LENGTH_LONG).show();
        }
        else if(PrescriptionType.equals("Type")){
            Snackbar.make(insertPresInDB,
                    "Please Choose a Prescription Type", Snackbar.LENGTH_LONG).show();
        }
        else if(NumberOFTakings.isEmpty()){
            Snackbar.make(insertPresInDB,
                    "Please Add the number of Takings", Snackbar.LENGTH_LONG).show();
        }

        else if(hours==null||minutes==null||am_pm==null){
            Snackbar.make(insertPresInDB,
                    "Please Choose  a Time", Snackbar.LENGTH_LONG).show();
        }
        else if(PrescriptionDose.equals("Dose")){
            Snackbar.make(insertPresInDB,
                    "Please Choose a Dose", Snackbar.LENGTH_LONG).show();
        }

    }

    /**
     * The method add doses of a a prescription to the dosetime prescription list.
     *
     * The user can add a maximum of 5 doses per prescription.
     *
     * The app notifies if the user added the max number of doses and ask them to either
     * delete some or add the prescription tot he db.
     *
     */
    private void addPrescriptionListView(){
        if(minutes.length()==1)
            minutes="0"+minutes;

        String TimeOfDose=hours+":"+minutes+":"+am_pm;

        if(maximumSizeOfTimeDoseList>0) {
            Prescription newPres=new Prescription(PrescriptionName,PrescriptionType, Integer.parseInt(NumberOFTakings),
                    DoctorName,DoctorNumber,Integer.parseInt(PrescriptionDose),TimeOfDose,MainActivity.signedInUser.getUserName());

            arrayList.add(newPres);
            ListTimeDoseAdapter.updateList();
            maximumSizeOfTimeDoseList--;
            Snackbar.make(insertPresInDB,
                    "You Added a new Time/Dosage", Snackbar.LENGTH_LONG).show();
            Log.d("maxA", "list max: "+maximumSizeOfTimeDoseList);

        }
        else{
            Snackbar.make(insertPresInDB,
                    "You cant add more, The List is full (remove from list or submit)", Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * The method clears all the gui elements in the add prescription form.
     *
     */
    private void clearGUIElements(){
      if(arrayList!=null) {
          arrayList.clear();
          maximumSizeOfTimeDoseList=5;
          Log.d("maxClear", "list max: "+maximumSizeOfTimeDoseList);

          ListTimeDoseAdapter.updateList();
      }

      if(presName!=null&&docName!=null&&docNumber!=null&&takings!=null) {
          presName.setText("");
          takings.setText("");
          docName.setText("");
          docNumber.setText("");
          PrescriptionName="";
          NumberOFTakings="";
          DoctorName="";
          DoctorNumber="";
          DoseSpinner.setSelection(0);
          PrescripitonTypeSpinner.setSelection(0);
          presName.requestFocus();
          Log.d("gui name", "setUserVisibleHint: " + PrescriptionName + ":" + DoctorName + ":" + DoctorNumber);
      }

      enableForm(true);
  }



////////////////////////////Asycn Tasks that are called in the AddPrescription Fragment/////////////////////////////////////////

    /**
     * The async task is responsible to:
     * 1- get the info from the form
     * 2- add the new prescription to the db.
     * 3- add the times of the doses to the db.
     * 4- generates a notification of that prescription.
     */
    private  class addPrescriptionAsyncTask extends AsyncTask<ArrayList<Prescription>, Void, Boolean> {

        private User u;

        @Override
        protected void onPreExecute() {

            insertPresInDB.setEnabled(false);
            progressDialog = new ProgressDialog(getContext(),
                    R.style.Theme_AppCompat_DayNight_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Adding a new Prescription!...");
            progressDialog.show();
        }
        @Override
        protected Boolean doInBackground(ArrayList<Prescription>... prescriptions) {

            ArrayList<Prescription> presc= prescriptions[0];
            u= MainActivity.signedInUser;
            String dosetime1="";
            String dosetime2="";
            String dosetime3="";
            String dosetime4="";
            String dosetime5="";

            switch(presc.size()){
                case 1:
                    dosetime1=presc.get(0).getDoseTime();
                    break;
                case 2:
                    dosetime1=presc.get(0).getDoseTime();
                    dosetime2=presc.get(1).getDoseTime();
                    break;
                case 3:
                    dosetime1=presc.get(0).getDoseTime();
                    dosetime2=presc.get(1).getDoseTime();
                    dosetime3=presc.get(2).getDoseTime();
                    break;
                case 4:
                    dosetime1=presc.get(0).getDoseTime();
                    dosetime2=presc.get(1).getDoseTime();
                    dosetime3=presc.get(2).getDoseTime();
                    dosetime4=presc.get(3).getDoseTime();
                    break;
                case 5:
                    dosetime1=presc.get(0).getDoseTime();
                    dosetime2=presc.get(1).getDoseTime();
                    dosetime3=presc.get(2).getDoseTime();
                    dosetime4=presc.get(3).getDoseTime();
                    dosetime5=presc.get(4).getDoseTime();
                    break;

            }


            String prescName=presc.get(0).getPrescriptionName();
            String prescType=presc.get(0).getPrescriptionType();
            int prescTakings=presc.get(0).getTakings();
            int forgetTakings=0;
            String docName=presc.get(0).getDoctorName();
            String docNum=presc.get(0).getDoctorNumber();
            int dose=presc.get(0).getPrescriptionDoese();
            String username= u.getUserName();
            AppDatabase db= SigninActivity.getDB();

            try {
                Prescription newPrescription = new Prescription(prescName, prescType, prescTakings, forgetTakings,
                        docName, docNum, dose, username);

                db.prescriptionDao().insertPrescription(newPrescription);

                DoseTime prescriptionTime = new DoseTime(dosetime1, dosetime2, dosetime3, dosetime4, dosetime5, prescName, username);
                db.dosetimeDao().insertPrescriptionTime(prescriptionTime);



                Notification n=new Notification(prescriptionTime,newPrescription,getActivity().getApplicationContext());

                return true;
            }catch (Exception e){
                Log.d("exception in inserting", "doInBackground: "+ e);

            }

            return false;
        }



        @Override
        protected void onPostExecute(Boolean insertingResult) {

            progressDialog.dismiss();
            insertPresInDB.setEnabled(true);

            if(insertingResult) {
                Snackbar.make(insertPresInDB,
                        "You added a new Prescription!", Snackbar.LENGTH_LONG).show();
            }
            else{
                Snackbar.make(insertPresInDB,
                        "Cant add the Prescription", Snackbar.LENGTH_LONG).show();

            }





        }


    }




/////////////////////////////////////////////Fragment override methods//////////////////////////////////////////////////////////

    @Override
    /**
     * When the fragment is available to the user call, clearGUIElements()
     */
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser&&view!=null){

            onResume();


        }



    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint())
             return;


        clearGUIElements();

    }






}
