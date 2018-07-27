package com.example.zeyad.prescriptionapp.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import java.util.Locale;

import static com.example.zeyad.prescriptionapp.R.color.blue;


/**

 */
public class PrescriptionLog extends Fragment {


    private View view;
    private ArrayList<Prescription> prescriptionsLogs;
    private ListAdapterPresLog adapterPresLog;
    private ListView Loglist;
    private ProgressDialog progressDialog;
    private EditText searchbox;
    private SwipeRefreshLayout SwipeRefreshLayout;


    public PrescriptionLog() {
        // Required empty public constructor

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_prescription_log, container, false);



        prescriptionLogGUI(view);

        return view;
    }

    private void prescriptionLogGUI(View view){


        Loglist = (ListView) view.findViewById(R.id.PresLogList);
        prescriptionsLogs=new ArrayList<Prescription>();
        searchbox=(EditText) view.findViewById(R.id.searchPrescriptionLog);
        SwipeRefreshLayout=(SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);

        adapterPresLog=new ListAdapterPresLog(this.getContext(), android.R.layout.simple_list_item_1, prescriptionsLogs);
        Loglist.setAdapter(adapterPresLog);
        new fetchPrescAsyncTask().execute(prescriptionsLogs);
        adapterPresLog.updateList();


        searchbox.setCompoundDrawablesWithIntrinsicBounds(
                R.mipmap.search, 0, 0, 0);

        searchbox.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = searchbox.getText().toString().toLowerCase(Locale.getDefault());
                adapterPresLog.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });

        SwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.orange),
                getResources().getColor(R.color.green),getResources().getColor(R.color.blue));
        SwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        refreshPrescriptionLog();

                    }
                }
        );




    }


    public  void refreshPrescriptionLog(){

        new fetchPrescAsyncTask().execute(prescriptionsLogs);
        SwipeRefreshLayout.setRefreshing(false);




    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser&&view!=null){

            if(prescriptionsLogs.size()==0)
                showEmptyLogNotification();



        }



    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint())
            return;


    }

    private void showEmptyLogNotification(){
        AlertDialog.Builder adb=new AlertDialog.Builder(getContext());
        adb.setTitle("Prescription Log");
        adb.setMessage(" Prescription Log is Emtpy. Add a prescription now ! ");

        adb.setNegativeButton("Ok", null);
        adb.show();

    }

    private  class  fetchPrescAsyncTask extends AsyncTask<ArrayList<Prescription>, Void, Boolean> {

        private User u;

        @Override
        protected void onPreExecute() {


        }
        @Override
        protected Boolean doInBackground(ArrayList<Prescription>... userPrescriptions) {

            ArrayList <Prescription> p= new ArrayList<Prescription>();
            AppDatabase db= SigninActivity.getDB();
            u=MainActivity.signedInUser;
            try {
                System.out.println("in in in:");

                userPrescriptions[0].clear();
                adapterPresLog.updateList();
                p.addAll(db.prescriptionDao().getUserPrescription(u.getUserName()));
                userPrescriptions[0].addAll(p);
                System.out.println("in in in:" + p.size());

                adapterPresLog.updateList();


                return true;
            }catch(Exception e){
                System.out.println(e.fillInStackTrace());

                System.out.println("in in in catch:" + p.size());


            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean Result) {


                Snackbar.make(getActivity().findViewById(android.R.id.content),
                        "Prescription Log Updated", Snackbar.LENGTH_LONG).show();






        }


    }
}
