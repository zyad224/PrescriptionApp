package com.example.zeyad.prescriptionapp.FindingNearbyPlaces;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Zeyad on 7/29/2018.
 *
 * This class is responsible to use the Google Place Api to find
 * nearby places in a radius of 3km from the user.
 *
 * It has 2 methods:
 * 1- executeSearchUrl: to find google place near the user
 * 2- DrawNearbyPlaces: to draw the place on the google map.
 */

public class FindNearby extends AsyncTask<Object, String, String> {


    String urlSearch;
    GoogleMap mMap;
    String googlePlaces;
    List<HashMap<String,String>> nearbyPlaces=null;

    @Override
    protected String doInBackground(Object... objects) {

        mMap=(GoogleMap)objects[0];
        urlSearch=(String)objects[1];

        try {
           googlePlaces= executeSearchURL(urlSearch);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googlePlaces;
    }

    @Override
    protected void onPostExecute(String result) {

      NearbyPlacesParser parser=new NearbyPlacesParser();
      nearbyPlaces=parser.parse(result);

      DrawNearbyPlaces(nearbyPlaces);

    }


    /**
     * The method recieves google place api url and execute it.
     *
     *
     * @param searchUrl
     * @return
     * @throws IOException
     */
    private String executeSearchURL(String searchUrl) throws IOException {
        String dataExecutedFromURL = "";
        String lineToRead = "";
        InputStream in = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(searchUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            in = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuffer sb = new StringBuffer();

            while ((lineToRead = br.readLine()) != null) {
                sb.append(lineToRead);
            }

            dataExecutedFromURL = sb.toString();
            br.close();

        } catch (Exception e) {
        } finally {
            in.close();
            urlConnection.disconnect();
        }
        return dataExecutedFromURL;
    }


    /**
     * The method recieves a list of hasmap of nearby places.
     * It iterates on the nearby places and draw them on the map
     * @param nearbyPlacesList
     */
    private void DrawNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);
            mMap.addMarker(markerOptions);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
    }


}

