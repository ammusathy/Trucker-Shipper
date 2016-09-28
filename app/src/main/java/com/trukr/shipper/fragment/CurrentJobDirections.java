package com.trukr.shipper.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.trukr.shipper.R;
import com.trukr.shipper.activity.DirectionsParser;
import com.trukr.shipper.model.ResponseParams.ShipmentResponse;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by nijamudhin on 5/27/2016.
 */
public class CurrentJobDirections extends Fragment {
    SharedPreferences preferences;
    RequestQueue queue;
    SharedPreferences.Editor editor;
    private GoogleMap googleMap;
    private final float DEFAULT_ZOOM_LEVEL = 13.0f;
    Double latstart = null, latEnd = null, longStart = null, longEnd = null;
    String fromadd, toadd;
    Context ctx;
    private GoogleMap map;
    View view;


    public CurrentJobDirections() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_directions, container, false);
        }
        ctx = getContext();

        SharedPreferences prefs = getActivity().getSharedPreferences("Details", Context.MODE_PRIVATE);
        String startlat = prefs.getString("FromLatitude", null);
        String startlong = prefs.getString("FromLongitude", null);
        String endlat = prefs.getString("ToLatitude", null);
        String endlong = prefs.getString("ToLongitude", null);
        System.out.println("startlat--->" + startlat + startlong + endlat + endlong);
        fromadd = prefs.getString("FromAddress", null);
        toadd = prefs.getString("ToAddress", null);
        latstart = Double.parseDouble(startlat);
        latEnd = Double.parseDouble(endlat);
        longStart = Double.parseDouble(startlong);
        longEnd = Double.parseDouble(endlong);
        map = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment)).getMap();
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latstart, longStart), 5));
        map.animateCamera(CameraUpdateFactory.zoomTo(5));
        String url = getDirectionsUrl();
        // Start downloading json data from Google Directions API
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);
        return view;
    }

    private String getDirectionsUrl() {
        // Origin of route
        String str_origin = "origin=" + latstart + "," + longStart;
        // Destination of route
        String str_dest = "destination=" + latEnd + "," + longEnd;
        // Sensor enabled
        String sensor = "sensor=false";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null)
                sb.append(line);
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d("Exception in url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";
            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                System.out.println("data = " + data);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsParser parser = new DirectionsParser();
                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions options = new MarkerOptions();
            /**
             * Starting point
             */
            LatLng start = new LatLng(latstart, longStart);
            map.addMarker(new MarkerOptions().position(start)/*.title("Marker in Pondy")*/.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_icon)).title(fromadd));
            map.moveCamera(CameraUpdateFactory.newLatLng(start));
            //options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            /**
             * Ending point
             */
            LatLng end = new LatLng(latEnd, longEnd);
            map.addMarker(new MarkerOptions().position(end)/*.title("Marker in Goa")*/.icon(BitmapDescriptorFactory.fromResource(R.drawable.imgpsh_fullsize)).title(toadd));
            //map.moveCamera(CameraUpdateFactory.newLatLng(end));
            //options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            try {
                // Traversing through all the routes
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();
                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);
                        points.add(position);
                    }
                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(10);
                    lineOptions.color(Color.parseColor("#00b3fd"));
                    // Drawing polyline in the Google Map for the i-th route
                    map.addPolyline(lineOptions);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

        }
    }
}
