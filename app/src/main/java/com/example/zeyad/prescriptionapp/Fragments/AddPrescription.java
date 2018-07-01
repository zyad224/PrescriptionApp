package com.example.zeyad.prescriptionapp.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.WindowDecorActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.zeyad.prescriptionapp.MainActivity;
import com.example.zeyad.prescriptionapp.R;

import java.sql.Time;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 * Use the {@link AddPrescription#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPrescription extends Fragment implements AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Spinner PillsSpinner,PrescripitonTypeSpinner;
    private TimePicker tp;
    private FloatingActionButton TimeDosefab;
    private ArrayAdapter presTypeAdapter;
    private ArrayAdapter presDoseAdapter;
    private ArrayAdapter<String> ListTimeDoseAdapter;
    private ListView TimeDoselist;
    private ArrayList<String> arrayList;
    private String[] PillsAmount={"Dose","1","2","3","4"};
    private String[] PrescriptionType={"Type","Pills","Syrup","Eyedrops","Injection"};
    private String hours,minutes,am_pm;
  //  private OnFragmentInteractionListener mListener;

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
        View view = inflater.inflate(R.layout.fragment_add_prescription, container, false);

        setPillsAmountSpinner(view);
        setPrescriptionTypeSpinner(view);
        setTimePicker(view);
        setTimeDoseList(view);


        return view;
    }

    private void setTimeDoseList(View view){



        TimeDoselist = (ListView) view.findViewById(R.id.TimeDoseList);
        TimeDosefab= (FloatingActionButton) view.findViewById(R.id.AddTimeDoseFab);
        arrayList = new ArrayList<String>();
        ListTimeDoseAdapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, arrayList);
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
                        ListTimeDoseAdapter.notifyDataSetChanged();
                    }});
                adb.show();
            }
        });

        TimeDosefab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation anim = android.view.animation.AnimationUtils.loadAnimation(TimeDosefab.getContext(),R.anim.shake);
                anim.setDuration(200L);
                TimeDosefab.startAnimation(anim);



                String Dose = PillsSpinner.getSelectedItem().toString();
                Log.d("fab:", "onTimeChanged: size:"+ hours+"-"+minutes+"-"+am_pm);

                if(Dose != null && !Dose.equals("Dose")&&(hours!=null&&minutes!=null&&am_pm!=null)){

                    if(minutes.length()==1)
                        minutes="0"+minutes;

                    String Time=hours+":"+minutes+":"+am_pm;

                    arrayList.add(Time+"  "+ Dose);
                    ListTimeDoseAdapter.notifyDataSetChanged();
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "You Added a new Time/Dosage", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Log.d("fab:", "onTimeChanged: k");
                }


            }
        });

    }

    private void setTimePicker(View view){

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

    private void setPrescriptionTypeSpinner(View view){
        PrescripitonTypeSpinner = (Spinner) view.findViewById(R.id.PrescriptionType);
        PrescripitonTypeSpinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        presTypeAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,PrescriptionType);
        presTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PrescripitonTypeSpinner.setAdapter(presTypeAdapter);
    }
    private void setPillsAmountSpinner(View view){
        PillsSpinner = (Spinner) view.findViewById(R.id.Dose);
        PillsSpinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        presDoseAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,PillsAmount);
        presDoseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PillsSpinner.setAdapter(presDoseAdapter);
    }
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
// TODO Auto-generated method stub

    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
