package com.example.zeyad.prescriptionapp.Fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.zeyad.prescriptionapp.Adapters.ListAdapterAddPres;
import com.example.zeyad.prescriptionapp.Database.AppDatabase;
import com.example.zeyad.prescriptionapp.Database.DoseTime;
import com.example.zeyad.prescriptionapp.Database.Notification;
import com.example.zeyad.prescriptionapp.Database.Prescription;
import com.example.zeyad.prescriptionapp.Database.User;
import com.example.zeyad.prescriptionapp.MainActivity;
import com.example.zeyad.prescriptionapp.R;
import com.example.zeyad.prescriptionapp.Services.NotificationService;
import com.example.zeyad.prescriptionapp.SigninActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.zip.Inflater;

import static android.content.Context.ALARM_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 * Use the {@link AddPrescription#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPrescription extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddPrescription.
     */
    // TODO: Rename and change types and number of parameters
    public static AddPrescription newInstance(String param1, String param2) {
        AddPrescription fragment = new AddPrescription();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_prescription, container, false);
        addPrescriptionGUI(view);
        return view;
    }


    private void addPrescriptionGUI(View view){

        initializeEditText(view);
        initializePrescriptionTypeSpinner(view);
        initializeTimePicker(view);
        initializeDoseAmountSpinner(view);
        initializeTimeDoseList(view);

    }

    private void initializeEditText(View view){
        presName=(EditText)view.findViewById(R.id.PrescriptionName);
        docName=(EditText)view.findViewById(R.id.DoctorName);
        docNumber=(EditText)view.findViewById(R.id.DoctorNumber);
        takings=(EditText)view.findViewById(R.id.Takings);
    }
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

        fabListeners();



        Log.d("list", "list content: "+arrayList);

    }

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

                if((!PrescriptionDose.equals("Dose"))&& (hours!=null&&minutes!=null&&am_pm!=null)
                        && (!PrescriptionType.equals("Type"))&&(!PrescriptionName.isEmpty()
                        && !DoctorName.isEmpty()&& !DoctorNumber.isEmpty() && !NumberOFTakings.isEmpty()))
                {
                    enableForm(false);
                    addPrescriptionListView();
                }
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


    private void enableForm(boolean decision){

        presName.setEnabled(decision);
        docName.setEnabled(decision);
        docNumber.setEnabled(decision);
        takings.setEnabled(decision);
        PrescripitonTypeSpinner.setEnabled(decision);
        DoseSpinner.setEnabled(decision);

    }
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
        else if(DoctorName.isEmpty()){
            Snackbar.make(insertPresInDB,
                    "Please Add a Doctor Name", Snackbar.LENGTH_LONG).show();
        }
        else if(DoctorNumber.isEmpty()){
            Snackbar.make(insertPresInDB,
                    "Please Add a Doctor Number", Snackbar.LENGTH_LONG).show();
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
                Log.d("Time:", "onTimeChanged: "+hours+ ":" + minutes + ":" + am_pm);
            }
        });
    }

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


                Notification n=new Notification(prescriptionTime,getActivity().getApplicationContext());
               // setNotification();

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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser&&view!=null){

            onResume();

            //This means this fragment is visible to user so you can write code to refresh the fragment here by reloaded the data.

        }

      //  Log.d("addpres", "onCreate: "+MainActivity.signedInUser.getUserName());


    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint())
             return;


        clearGUIElements();

    }






}
