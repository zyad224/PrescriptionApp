package com.example.zeyad.prescriptionapp.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.zeyad.prescriptionapp.MainActivity;
import com.example.zeyad.prescriptionapp.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.Calendar;


public class Home extends Fragment {


    private View view;
    private TextView greetings;
    private ListView upcomingPrescriptionsList;
    private PieChart chart;
    public Home() {
        // Required empty public constructor

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_home, container, false);

        homeGUI(view);
        greetUser();
        setUpcomingPrescriptionList();
        setPrescriptionChart();

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser&&view!=null){

          clearGreeting();
          greetUser();


        }

    }

    private void homeGUI(View view){

        greetings=(TextView) view.findViewById(R.id.Greetings);
        chart = (PieChart) view.findViewById(R.id.chart);
        upcomingPrescriptionsList=view.findViewById(R.id.upcomingPrescriptions);




    }

    private void setUpcomingPrescriptionList(){
        ArrayAdapter adapter = new ArrayAdapter<String>(this.getContext(),
                android.R.layout.simple_list_item_1, MainActivity.upcomingPrescription);
        upcomingPrescriptionsList.setAdapter(adapter);

    }

    private void setPrescriptionChart(){
        PieDataSet set = new PieDataSet(MainActivity.chartEntries, "Prescriptions Takings %");
        PieData data = new PieData(set);
        chart.setData(data);
        chart.setNoDataText("No Data Available, Add a prescription");
        chart.setHovered(true);
        chart.setDrawEntryLabels(true);
        chart.setDrawHoleEnabled(true);
        chart.setDrawSlicesUnderHole(true);
        chart.setHoleColor(Color.RED);
        chart.setBackgroundColor(Color.BLACK);
        chart.setEntryLabelTextSize(12);
        chart.setEntryLabelColor(Color.RED);
        chart.setCameraDistance(12);
        chart.setFocusable(true);
        chart.setMotionEventSplittingEnabled(true);
        chart.invalidate();

    }

    private void greetUser(){

        Calendar c= Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        String greeting=null;

        if(timeOfDay>=1 && timeOfDay<=12){
            greeting = "Good Morning";
        } else if(timeOfDay>=12 && timeOfDay<=16){
            greeting = "Good Afternoon";
        } else if(timeOfDay>=16 && timeOfDay<=21){
            greeting = "Good Evening";
        } else if(timeOfDay>=21 && timeOfDay<=24){
            greeting = "Good Night";
        }

        greetings.setTextSize(getResources().getDimension(R.dimen.greeting_size));
        greetings.setText(greeting +","+ MainActivity.signedInUser.getName());

    }

    private void clearGreeting(){
        greetings.setText("");
    }


}
