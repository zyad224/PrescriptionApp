package com.example.zeyad.prescriptionapp.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.zeyad.prescriptionapp.Adapters.ListAdapterPresLog;
import com.example.zeyad.prescriptionapp.Database.AppDatabase;
import com.example.zeyad.prescriptionapp.Database.Prescription;
import com.example.zeyad.prescriptionapp.Database.User;
import com.example.zeyad.prescriptionapp.MainActivity;
import com.example.zeyad.prescriptionapp.R;
import com.example.zeyad.prescriptionapp.SigninActivity;

import java.util.ArrayList;
import java.util.List;


/**

 */
public class PrescriptionLog extends Fragment {


    private View view;
    private ArrayList<Prescription> prescriptionsLogs;
    private ListAdapterPresLog adapterPresLog;
    private ListView Loglist;
    private ProgressDialog progressDialog;


    public PrescriptionLog() {
        // Required empty public constructor

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_prescription_log, container, false);
        Loglist = (ListView) view.findViewById(R.id.PresLogList);
        prescriptionsLogs=new ArrayList<Prescription>();
        adapterPresLog=new ListAdapterPresLog(this.getContext(), android.R.layout.simple_list_item_1, prescriptionsLogs);
        Loglist.setAdapter(adapterPresLog);

        new fetchPrescAsyncTask().execute(prescriptionsLogs);
        adapterPresLog.updateList();




        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser&&view!=null){

            System.out.println("inside zez");
            Loglist = (ListView) view.findViewById(R.id.PresLogList);
            prescriptionsLogs=new ArrayList<Prescription>();
            adapterPresLog=new ListAdapterPresLog(this.getContext(), android.R.layout.simple_list_item_1, prescriptionsLogs);
            Loglist.setAdapter(adapterPresLog);
            Loglist.setVisibility(view.VISIBLE);

            new fetchPrescAsyncTask().execute(prescriptionsLogs);
            adapterPresLog.updateList();


        }




    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint())
            return;


    }

    private  class  fetchPrescAsyncTask extends AsyncTask<ArrayList<Prescription>, Void, Boolean> {

        private User u;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(getContext(),
                    R.style.Theme_AppCompat_DayNight_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Fetching your prescriptions...");
            progressDialog.show();
        }
        @Override
        protected Boolean doInBackground(ArrayList<Prescription>... userPrescriptions) {

            ArrayList <Prescription> p= userPrescriptions[0];
            AppDatabase db= SigninActivity.getDB();
            u=MainActivity.signedInUser;
            try {
                System.out.println("in in in:");

                p.clear();
                adapterPresLog.updateList();
                p.addAll(db.prescriptionDao().getUserPrescription(u.getUserName()));
                System.out.println("in in in:" + p.size());

                adapterPresLog.updateList();


                return true;
            }catch(Exception e){
                System.out.println("in in in catch:" + p.size());


            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean Result) {

            progressDialog.dismiss();

            if(Result) {
                Snackbar.make(getActivity().findViewById(android.R.id.content),
                        "Prescription Log Updated", Snackbar.LENGTH_LONG).show();
            }
            else{
                Snackbar.make(getActivity().findViewById(android.R.id.content),
                        "Cant Update Prescription Log at the moment", Snackbar.LENGTH_LONG).show();
            }




        }


    }
}
