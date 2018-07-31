package com.example.zeyad.prescriptionapp.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.zeyad.prescriptionapp.Acitvities.MainActivity;
import com.example.zeyad.prescriptionapp.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.Calendar;


/**
 * This is the home fragment.
 * It initializes the interface of the home fragment .
 * It greets the user.
 * It shows the upcoming prescription list.
 * It shows a chart of prescription percentage completion.
 *
 */
public class Home extends Fragment {


    private View view;
    private TextView greetings;
    private TextView emptyList;
    private ListView upcomingPrescriptionsList;
    private PieChart chart;
    public Home() {
        // Required empty public constructor

    }



//////////////////////////////Home Fragment starts here/////////////////////////////////////////////////////////

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




//////////////////////////////Home Fragment Methods/////////////////////////////////////////////////////////

    /**
     *
     *
     * The method is responsible to set the GUI of Home Fragment.
     * It sets:
     * 1- the greeting text view.
     * 2- the upcoming prescription list.
     * 3- the chart.
     * 4- an empty list in case of no upcoming prescriptions.
     *
     *
     * @param view
     */
    private void homeGUI(View view){

        greetings=(TextView) view.findViewById(R.id.Greetings);
        emptyList=(TextView)view.findViewById(R.id.empty);
        chart = (PieChart) view.findViewById(R.id.chart);
        upcomingPrescriptionsList=view.findViewById(R.id.upcomingPrescriptions);






    }

    /**
     * The method responsible to:
     * 1- set the adapter of the upcoming prescription list.
     * 2- show an empty list if there is no any upcoming prescriptions.
     *
     */
    private void setUpcomingPrescriptionList(){
        ArrayAdapter adapter = new ArrayAdapter<String>(this.getContext(),
                android.R.layout.simple_list_item_1, MainActivity.upcomingPrescription);
        upcomingPrescriptionsList.setAdapter(adapter);

        if(MainActivity.upcomingPrescription.isEmpty()){
            upcomingPrescriptionsList.setVisibility(View.INVISIBLE);
            emptyList.setVisibility(View.VISIBLE);

        }

    }

    /**
     * The method is responsible to:
     * 1- set the prescription with their completion percentage in the chart.
     * 2- set different options to the chart.
     * 3- set background color of the chart.
     * 4- set camera distance , etc.
     */
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

    /**
     * The method shows greeting time to the user depending on the time of the day.
     * {morning, afternoon, evening, night}
     */
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

    /**
     * The method clear the greeting text view in order to
     * re-display another greeting message.
     */
    private void clearGreeting(){
        greetings.setText("");
    }


    @Override
    /**
     * The method is called when the fragment is shown to the user.
     *
     * When the Home Fragment is shown to the user:
     * 1- clear the greeting text view.
     * 2- call greetuser()
     */
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser&&view!=null){

            clearGreeting();
            greetUser();


        }

    }
}
